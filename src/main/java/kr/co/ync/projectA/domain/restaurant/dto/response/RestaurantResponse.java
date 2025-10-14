package kr.co.ync.projectA.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantResponse {
    private Long id;
    private Long ownerId;
    private String ownerEmail;
    private String name;
    private String address;
    private String area;
    private String phone;
    private String image;
}
