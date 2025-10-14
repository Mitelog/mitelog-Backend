package kr.co.ync.projectA.domain.restaurant.repository;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    // 지역별 조회
    List<RestaurantEntity> findByArea(String area);

    // 이름 검색
    List<RestaurantEntity> findByNameContaining(String keyword);

    // 카테고리별 조회 (카테고리 조인)
    @Query(
            value = """
            select distinct r
            from RestaurantEntity r
            join r.categoryMappings m
            join m.category c
            where c.name = :categoryName
        """,
            countQuery = """
            select count(distinct r)
            from RestaurantEntity r
            join r.categoryMappings m
            join m.category c
            where c.name = :categoryName
        """
    )
    Page<RestaurantEntity> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
}

