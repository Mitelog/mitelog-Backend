package kr.co.ync.projectA.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Restaurant {
    private Long id;
    private Long ownerId;
    private String ownerEmail;
    private String name;
    private String address;
    private String area;
    private String phone;
    private String image;
}
