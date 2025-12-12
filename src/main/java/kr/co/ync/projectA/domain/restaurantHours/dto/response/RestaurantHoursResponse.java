package kr.co.ync.projectA.domain.restaurantHours.dto.response;

import kr.co.ync.projectA.domain.restaurantHours.entity.RestaurantHoursEntity;
import kr.co.ync.projectA.domain.restaurantHours.enums.DayOfWeekType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantHoursResponse {

    private DayOfWeekType dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isOpen;

    public static RestaurantHoursResponse fromEntity(RestaurantHoursEntity entity) {
        return RestaurantHoursResponse.builder()
                .dayOfWeek(entity.getDayOfWeek())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .isOpen(entity.getIsOpen())
                .build();
    }
}
