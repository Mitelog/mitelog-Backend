package kr.co.ync.projectA.domain.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    // 지역별 조회
    List<RestaurantEntity> findByArea(String area);

    // 카테고리별 페이징 조회
    Page<RestaurantEntity> findByCategory(String category, Pageable pageable);

    // 이름 검색
    List<RestaurantEntity> findByNameContaining(String keyword);
}
