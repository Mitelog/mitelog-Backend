package kr.co.ync.projectA.domain.restaurant.repository;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    /**
     * ✅ 통합 검색 / 필터링 쿼리 (JPQL)
     *
     * 프론트에서 /api/restaurants?keyword=ラーメン&region=東京都&category=和食
     * 이런 형태로 요청 시 작동합니다.
     *
     * - keyword: 가게명(name) 또는 주소(address)에 포함된 문자열 검색
     * - region: 주소(address)에 지역명 포함 시 매칭
     * - category: 카테고리 이름이 일치하는 경우 매칭
     *
     * 3개 중 하나라도 null이면 해당 조건은 자동으로 무시됩니다.
     */
    @Query("""
        SELECT DISTINCT r
        FROM RestaurantEntity r
        LEFT JOIN r.categoryMappings m
        LEFT JOIN m.category c
        WHERE (:keyword IS NULL OR r.name LIKE %:keyword% OR r.address LIKE %:keyword%)
        AND (:region IS NULL OR r.address LIKE %:region%)
        AND (:category IS NULL OR c.name = :category)
    """)
    Page<RestaurantEntity> findByConditions(
            @Param("keyword") String keyword,
            @Param("region") String region,
            @Param("category") String category,
            Pageable pageable
    );
}
