package kr.co.ync.projectA.domain.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

//    List<RestaurantEntity> findByArea(String area);
//
//    Page<RestaurantEntity> findByCategory(String category, Pageable pageable);
//
//    List<RestaurantEntity> findByNameContaining(String keyword);
}

