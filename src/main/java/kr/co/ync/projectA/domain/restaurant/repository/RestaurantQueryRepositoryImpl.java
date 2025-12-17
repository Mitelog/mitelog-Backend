package kr.co.ync.projectA.domain.restaurant.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.ync.projectA.domain.category.entity.QCategoryEntity;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantSearchRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.QRestaurantCategoryMapEntity;
import kr.co.ync.projectA.domain.restaurantDetail.entity.QRestaurantDetailEntity;
import kr.co.ync.projectA.domain.restaurantDetail.entity.PaymentMethod; // ✅ 실제 패키지 경로 확인해서 수정
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
         * - JOIN이 많아지면 row가 늘어나서 페이징이 깨질 수 있음
         * - 그래서 "ID만 distinct로 뽑고" offset/limit 적용하는 방식이 안전함
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
         * - mapper 재사용
         * - categoryMappings/category까지 fetchJoin 해서 N+1 줄임
         * - IDs 순서를 유지해야 페이지 정렬이 깨지지 않음
         *   => CaseBuilder로 "ids 순서대로" orderBy
         * ========================================================= */
        OrderSpecifier<Integer> orderByIds = buildOrderByIds(ids, r);

        List<RestaurantResponse> content = queryFactory
                .selectFrom(r)
                .leftJoin(r.categoryMappings, rcm).fetchJoin()
                .leftJoin(rcm.category, c).fetchJoin()
                .leftJoin(r.owner).fetchJoin() // ownerName/Email 등을 mapper가 필요로 하면 N+1 방지
                .where(r.id.in(ids))
                .distinct()
                .orderBy(orderByIds)
                .fetch()
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();

        /* =========================================================
         * 3) count 쿼리
         * - totalPages 계산용
         * - IDs 쿼리와 동일한 where 조건을 적용해야 정확함
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
     * - Pageable의 sort를 가능한 범위에서 반영
     * - 프로젝트에서 실제로 쓰는 컬럼명(createDateTime 등)에 맞춰야 함
     * - 모르면 일단 id desc로 고정해도 OK
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

            // ✅ 자주 쓰는 정렬만 매핑 (필요하면 계속 추가)
            switch (prop) {
                case "id" -> list.add(new OrderSpecifier<>(direction, r.id));
                case "name" -> list.add(new OrderSpecifier<>(direction, r.name));
                case "area" -> list.add(new OrderSpecifier<>(direction, r.area));
                case "averageRating" -> list.add(new OrderSpecifier<>(direction, r.averageRating));
                // BaseTimeEntity 필드명이 createDateTime라면 아래처럼:
                case "createDateTime" -> list.add(new OrderSpecifier<>(direction, r.createDateTime));
                default -> {
                    // 알 수 없는 정렬 필드는 무시(안 터지게)
                }
            }
        });

        if (list.isEmpty()) {
            return new OrderSpecifier<?>[]{ r.id.desc() };
        }
        return list.toArray(new OrderSpecifier<?>[0]);
    }

    /**
     * ✅ IDs 순서 보장용 OrderSpecifier
     * - 첫 번째 쿼리(ids)는 정렬/페이징이 적용됨
     * - 두 번째 쿼리(where in ids)는 그냥 fetch하면 DB가 임의 순서로 줌
     * - 그래서 CaseBuilder로 ids 순서대로 정렬을 강제한다.
     */
    private OrderSpecifier<Integer> buildOrderByIds(List<Long> ids, QRestaurantEntity r) {
        CaseBuilder.Cases<Integer, Integer> cb = new CaseBuilder().when(r.id.eq(ids.get(0))).then(0);
        for (int i = 1; i < ids.size(); i++) {
            cb = cb.when(r.id.eq(ids.get(i))).then(i);
        }
        return cb.otherwise(ids.size()).asc();
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
     * - false까지 조건 걸면 "false인 가게만" 찾아버림 (UX 파탄)
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
