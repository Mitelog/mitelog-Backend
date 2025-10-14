package kr.co.ync.projectA.domain.restaurant.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantRequest {
    private String name;
    private String address;
    private String area;
    private String phone;
    private String image;
}
