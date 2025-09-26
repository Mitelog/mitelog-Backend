package kr.co.ync.projectA.domain.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
}
