package kr.co.ync.projectA.domain.restaurantHoildays.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurantHoildays.entity.RestaurantHolidaysEntity;

public interface RestaurantHolidaysRepository extends JpaRepository<RestaurantHolidaysEntity, Long> {
}

