package kr.co.ync.projectA.domain.restaurant.mapper;

import kr.co.ync.projectA.domain.restaurant.dto.Restaurant;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

public class RestaurantMapper {

    public static Restaurant toDTO(RestaurantEntity entity) {
        if (entity == null) return null;
        return Restaurant.builder()
                .id(entity.getId())
                .ownerId(entity.getOwner().getId())
                .ownerEmail(entity.getOwner().getEmail())
                .name(entity.getName())
                .address(entity.getAddress())
                .area(entity.getArea())
                .phone(entity.getPhone())
                .image(entity.getImage())
                .build();
    }

    public static RestaurantEntity toEntity(Restaurant dto) {
        if (dto == null) return null;
        return RestaurantEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .area(dto.getArea())
                .phone(dto.getPhone())
                .image(dto.getImage())
                .build();
    }
}
