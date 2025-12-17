package kr.co.ync.projectA.domain.restaurantHours.repository;

import kr.co.ync.projectA.domain.restaurantHours.enums.DayOfWeekType;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurantHours.entity.RestaurantHoursEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantHoursRepository extends JpaRepository<RestaurantHoursEntity, Long> {
    List<RestaurantHoursEntity> findByRestaurant_Id(Long restaurantId);

    Optional<RestaurantHoursEntity> findByRestaurant_IdAndDayOfWeek(Long restaurantId, DayOfWeekType dayOfWeek);

    void deleteByRestaurant_Id(Long restaurantId);
}
