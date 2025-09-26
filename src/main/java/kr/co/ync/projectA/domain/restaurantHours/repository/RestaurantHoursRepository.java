package kr.co.ync.projectA.domain.restaurantHours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurantHours.entity.RestaurantHoursEntity;

public interface RestaurantHoursRepository extends JpaRepository<RestaurantHoursEntity, Long> {
}
