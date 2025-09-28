package kr.co.ync.projectA.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Restaurant {

    private Long id;

    private Long ownerId;     // 등록한 회원 ID
    private String ownerEmail; // 필요하다면 이메일 추가

    private String name;
    private String address;
    private String area;
    private String phone;
    private String image;
}
