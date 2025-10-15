package kr.co.ync.projectA.domain.menu.mapper;

import kr.co.ync.projectA.domain.menu.dto.request.MenuRequest;
import kr.co.ync.projectA.domain.menu.dto.response.MenuResponse;
import kr.co.ync.projectA.domain.menu.entity.MenuEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

public class MenuMapper {

    // DTO → Entity
    public static MenuEntity toEntity(MenuRequest dto, RestaurantEntity restaurant) {
        return MenuEntity.builder()
                .restaurant(restaurant)
                .name(dto.getName())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .build();
    }

    // Entity → DTO
    public static MenuResponse toResponse(MenuEntity entity) {
        return MenuResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    // 업데이트용
    public static void updateEntity(MenuEntity entity, MenuRequest dto) {
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());
    }
}
