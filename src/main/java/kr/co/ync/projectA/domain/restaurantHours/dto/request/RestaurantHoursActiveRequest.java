package kr.co.ync.projectA.domain.restaurantHours.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantHoursActiveRequest {
    private Boolean isOpen;
}
