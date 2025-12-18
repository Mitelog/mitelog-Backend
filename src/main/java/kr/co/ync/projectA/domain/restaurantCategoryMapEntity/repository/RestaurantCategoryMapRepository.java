package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;

import java.util.List;

public interface RestaurantCategoryMapRepository extends JpaRepository<RestaurantCategoryMapEntity, Long> {
    void deleteByRestaurant_Id(Long restaurantId);
    List<RestaurantCategoryMapEntity> findByRestaurant_Id(Long restaurantId);
}
