package kr.co.ync.projectA.domain.restaurant.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantSearchRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurantDetail.entity.QRestaurantDetailEntity;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.QRestaurantCategoryMapEntity;
import kr.co.ync.projectA.domain.category.entity.QCategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantQueryRepositoryImpl implements RestaurantQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RestaurantResponse> search(RestaurantSearchRequest cond, Pageable pageable) {

        QRestaurantEntity r = QRestaurantEntity.restaurantEntity;

        // ✅ 너희 디테일 엔티티 Q타입 이름은 실제 생성된 것으로 맞춰줘야 함
        QRestaurantDetailEntity d = QRestaurantDetailEntity.restaurantDetailEntity;

        QRestaurantCategoryMapEntity rcm = QRestaurantCategoryMapEntity.restaurantCategoryMapEntity;
        QCategoryEntity c = QCategoryEntity.categoryEntity;

        // =========================
        // 1) content query
        // =========================
        List<Long> ids = queryFactory
                .select(r.id)
                .from(r)
                // ✅ detail 조인 (필터 조건이 있을 수 있으므로 leftJoin)
                .leftJoin(d).on(d.restaurantId.id.eq(r.id))
                // ✅ 카테고리 조인(드롭다운 필터)
                .leftJoin(rcm).on(rcm.restaurant.id.eq(r.id))
                .leftJoin(c).on(rcm.category.id.eq(c.id))
                .where(
                        keywordContains(cond.getKeyword(), r),
                        areaEq(cond.getArea(), r),
                        categoryEq(cond.getCategory(), c),
                        parkingEq(cond.getParkingArea(), d),
                        privateRoomEq(cond.getPrivateRoom(), d),
                        smokingEq(cond.getSmoking(), d),
                        unlimitDrinkEq(cond.getUnlimitDrink(), d),
                        unlimitFoodEq(cond.getUnlimitFood(), d)
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // =========================
        // 2) 실제 엔티티 조회(Mapper 재사용)
        // - 필요하면 fetch join으로 N+1 줄일 수 있음
        // =========================
        List<RestaurantResponse> content = queryFactory
                .selectFrom(r)
                .where(r.id.in(ids))
                .fetch()
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();

        // =========================
        // 3) count query
        // =========================
        Long total = queryFactory
                .select(r.id.countDistinct())
                .from(r)
                .leftJoin(d).on(d.restaurantId.id.eq(r.id))
                .leftJoin(rcm).on(rcm.restaurant.id.eq(r.id))
                .leftJoin(c).on(rcm.category.id.eq(c.id))
                .where(
                        keywordContains(cond.getKeyword(), r),
                        areaEq(cond.getArea(), r),
                        categoryEq(cond.getCategory(), c),
                        parkingEq(cond.getParkingArea(), d),
                        privateRoomEq(cond.getPrivateRoom(), d),
                        smokingEq(cond.getSmoking(), d),
                        unlimitDrinkEq(cond.getUnlimitDrink(), d),
                        unlimitFoodEq(cond.getUnlimitFood(), d)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    /* =========================
       조건 빌더들 (null이면 조건 미적용)
       ========================= */

    private BooleanExpression keywordContains(String keyword, QRestaurantEntity r) {
        return (keyword == null || keyword.isBlank()) ? null : r.name.containsIgnoreCase(keyword);
    }

    private BooleanExpression areaEq(String area, QRestaurantEntity r) {
        return (area == null || area.isBlank() || "すべての地域".equals(area)) ? null : r.area.eq(area);
    }

    private BooleanExpression categoryEq(String category, QCategoryEntity c) {
        return (category == null || category.isBlank() || "すべてのカテゴリ".equals(category)) ? null : c.name.eq(category);
    }

    private BooleanExpression parkingEq(Boolean v, QRestaurantDetailEntity d) {
        return (v == null) ? null : d.parkingArea.eq(v);
    }

    private BooleanExpression privateRoomEq(Boolean v, QRestaurantDetailEntity d) {
        return (v == null) ? null : d.privateRoom.eq(v);
    }

    private BooleanExpression smokingEq(Boolean v, QRestaurantDetailEntity d) {
        return (v == null) ? null : d.smoking.eq(v);
    }

    private BooleanExpression unlimitDrinkEq(Boolean v, QRestaurantDetailEntity d) {
        return (v == null) ? null : d.unlimitDrink.eq(v);
    }

    private BooleanExpression unlimitFoodEq(Boolean v, QRestaurantDetailEntity d) {
        return (v == null) ? null : d.unlimitFood.eq(v);
    }
}
