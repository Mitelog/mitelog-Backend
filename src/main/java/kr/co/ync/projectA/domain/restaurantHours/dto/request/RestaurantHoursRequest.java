package kr.co.ync.projectA.domain.restaurantHours.dto.request;

import kr.co.ync.projectA.domain.restaurantHours.enums.DayOfWeekType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantHoursRequest {
    private DayOfWeekType dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isOpen;
}
