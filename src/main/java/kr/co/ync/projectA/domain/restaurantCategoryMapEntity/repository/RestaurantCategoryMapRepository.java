package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;

public interface RestaurantCategoryMapRepository extends JpaRepository<RestaurantCategoryMapEntity, Long> {
}
