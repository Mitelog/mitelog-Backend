package kr.co.ync.projectA.domain.restaurantDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurantDetail.entity.RestaurantDetailEntity;

public interface RestaurantDetailRepository extends JpaRepository<RestaurantDetailEntity, Long> {
}
