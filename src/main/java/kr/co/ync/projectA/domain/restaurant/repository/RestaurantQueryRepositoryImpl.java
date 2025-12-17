package kr.co.ync.projectA.domain.restaurant.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.ync.projectA.domain.category.entity.QCategoryEntity;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantSearchRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.QRestaurantCategoryMapEntity;
import kr.co.ync.projectA.domain.restaurantDetail.entity.PaymentMethod; // ✅ PaymentMethod는 detail 쪽
import kr.co.ync.projectA.domain.restaurantDetail.entity.QRestaurantDetailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepositoryImpl implements RestaurantQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RestaurantResponse> search(RestaurantSearchRequest cond, Pageable pageable) {

        QRestaurantEntity r = QRestaurantEntity.restaurantEntity;
        QRestaurantDetailEntity d = QRestaurantDetailEntity.restaurantDetailEntity;
        QRestaurantCategoryMapEntity rcm = QRestaurantCategoryMapEntity.restaurantCategoryMapEntity;
        QCategoryEntity c = QCategoryEntity.categoryEntity;

        /* =========================================================
         * 1) IDs 페이징 쿼리
         * ========================================================= */
        List<Long> ids = queryFactory
                .select(r.id)
                .from(r)

                // ✅ (중요) detail 조인: d를 직접 조인해야 d.xxx 조건이 정상 동작
                .leftJoin(d).on(d.restaurant.id.eq(r.id))

                // ✅ category 조인 (드롭다운 필터)
                .leftJoin(rcm).on(rcm.restaurant.id.eq(r.id))
                .leftJoin(c).on(rcm.category.id.eq(c.id))

                .where(
                        keywordContains(cond.getKeyword(), r),
                        areaEq(cond.getArea(), r),
                        categoryEq(cond.getCategory(), c),

                        // ✅ creditCard는 RestaurantDetailEntity.paymentMethods 기준
                        creditCardEq(cond.getCreditCard(), d),

                        // ✅ 디테일 boolean 조건들
                        parkingEq(cond.getParkingArea(), d),
                        privateRoomEq(cond.getPrivateRoom(), d),
                        smokingEq(cond.getSmoking(), d),
                        unlimitDrinkEq(cond.getUnlimitDrink(), d),
                        unlimitFoodEq(cond.getUnlimitFood(), d)
                )
                .distinct()

                // ✅ 정렬 적용 (pageable sort 있으면 반영, 없으면 기본 id desc)
                .orderBy(resolveOrderSpecifiers(pageable, r))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        /* =========================================================
         * 2) 실제 엔티티 조회
         * - categoryMappings/category/owner fetchJoin으로 N+1 완화
         * - ids 순서 유지(CaseBuilder)
         * ========================================================= */
        OrderSpecifier<Integer> orderByIds = buildOrderByIds(ids, r);

        List<RestaurantResponse> content = queryFactory
                .selectFrom(r)
                .leftJoin(r.owner).fetchJoin()
                .leftJoin(r.categoryMappings, rcm).fetchJoin()
                .leftJoin(rcm.category, c).fetchJoin()
                .where(r.id.in(ids))
                .distinct()
                .orderBy(orderByIds)
                .fetch()
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();

        /* =========================================================
         * 3) count 쿼리
         * ========================================================= */
        Long total = queryFactory
                .select(r.id.countDistinct())
                .from(r)
                .leftJoin(d).on(d.restaurant.id.eq(r.id))
                .leftJoin(rcm).on(rcm.restaurant.id.eq(r.id))
                .leftJoin(c).on(rcm.category.id.eq(c.id))
                .where(
                        keywordContains(cond.getKeyword(), r),
                        areaEq(cond.getArea(), r),
                        categoryEq(cond.getCategory(), c),

                        creditCardEq(cond.getCreditCard(), d),

                        parkingEq(cond.getParkingArea(), d),
                        privateRoomEq(cond.getPrivateRoom(), d),
                        smokingEq(cond.getSmoking(), d),
                        unlimitDrinkEq(cond.getUnlimitDrink(), d),
                        unlimitFoodEq(cond.getUnlimitFood(), d)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    /* =========================================================
     * 정렬 처리
     * ========================================================= */
    private OrderSpecifier<?>[] resolveOrderSpecifiers(Pageable pageable, QRestaurantEntity r) {
        Sort sort = pageable.getSort();
        if (sort == null || sort.isUnsorted()) {
            return new OrderSpecifier<?>[]{ r.id.desc() };
        }

        List<OrderSpecifier<?>> list = new ArrayList<>();

        sort.forEach(order -> {
            String prop = order.getProperty();
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            // ✅ 필요한 정렬만 매핑 (프로젝트 필드명에 맞춰 조정)
            switch (prop) {
                case "id" -> list.add(new OrderSpecifier<>(direction, r.id));
                case "name" -> list.add(new OrderSpecifier<>(direction, r.name));
                case "area" -> list.add(new OrderSpecifier<>(direction, r.area));
                case "averageRating" -> list.add(new OrderSpecifier<>(direction, r.averageRating));
                case "createDateTime" -> list.add(new OrderSpecifier<>(direction, r.createDateTime)); // BaseTimeEntity 필드명
                default -> {
                    // 알 수 없는 정렬 필드는 무시
                }
            }
        });

        if (list.isEmpty()) {
            return new OrderSpecifier<?>[]{ r.id.desc() };
        }
        return list.toArray(new OrderSpecifier<?>[0]);
    }

    /**
     * ✅ ids 순서 유지용 orderBy (컴파일 에러 안 나는 버전)
     */
    private OrderSpecifier<Integer> buildOrderByIds(List<Long> ids, QRestaurantEntity r) {

        CaseBuilder.Cases<Integer, NumberExpression<Integer>> cases = null;

        for (int i = 0; i < ids.size(); i++) {
            if (i == 0) {
                cases = new CaseBuilder()
                        .when(r.id.eq(ids.get(i))).then(i);
            } else {
                cases = cases
                        .when(r.id.eq(ids.get(i))).then(i);
            }
        }

        NumberExpression<Integer> orderExpr = cases.otherwise(ids.size());
        return orderExpr.asc();
    }

    /* =========================================================
     * 조건 빌더 (null 반환 시 조건 미적용)
     * ========================================================= */

    private BooleanExpression keywordContains(String keyword, QRestaurantEntity r) {
        if (keyword == null || keyword.isBlank()) return null;
        return r.name.containsIgnoreCase(keyword);
    }

    private BooleanExpression areaEq(String area, QRestaurantEntity r) {
        if (area == null || area.isBlank() || "すべての地域".equals(area)) return null;
        return r.area.eq(area);
    }

    private BooleanExpression categoryEq(String category, QCategoryEntity c) {
        if (category == null || category.isBlank() || "すべてのカテゴリ".equals(category)) return null;
        return c.name.eq(category);
    }

    /**
     * ✅ creditCard
     * - RestaurantDetailEntity.paymentMethods(Set<PaymentMethod>) 안에 CREDIT_CARD 포함이면 OK
     * - 체크된 경우(true)만 필터로 동작
     */
    private BooleanExpression creditCardEq(Boolean v, QRestaurantDetailEntity d) {
        if (v == null || !v) return null;
        return d.paymentMethods.contains(PaymentMethod.CREDIT_CARD);
    }

    /**
     * ✅ boolean 필터는 "true일 때만" 적용
     */
    private BooleanExpression parkingEq(Boolean v, QRestaurantDetailEntity d) {
        if (v == null || !v) return null;
        return d.parkingArea.isTrue();
    }

    private BooleanExpression privateRoomEq(Boolean v, QRestaurantDetailEntity d) {
        if (v == null || !v) return null;
        return d.privateRoom.isTrue();
    }

    private BooleanExpression smokingEq(Boolean v, QRestaurantDetailEntity d) {
        if (v == null || !v) return null;
        return d.smoking.isTrue();
    }

    private BooleanExpression unlimitDrinkEq(Boolean v, QRestaurantDetailEntity d) {
        if (v == null || !v) return null;
        return d.unlimitDrink.isTrue();
    }

    private BooleanExpression unlimitFoodEq(Boolean v, QRestaurantDetailEntity d) {
        if (v == null || !v) return null;
        return d.unlimitFood.isTrue();
    }
}
