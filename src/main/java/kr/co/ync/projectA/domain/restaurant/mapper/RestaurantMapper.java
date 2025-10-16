package kr.co.ync.projectA.domain.restaurant.mapper;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import java.util.List;

public class RestaurantMapper {

    public static RestaurantEntity toEntity(RestaurantRequest dto, MemberEntity owner) {
        return RestaurantEntity.builder()
                .owner(owner)
                .name(dto.getName())
                .address(dto.getAddress())
                .area(dto.getArea())
                .phone(dto.getPhone())
                .image(dto.getImage())
                .build();
    }

    public static RestaurantResponse toResponse(RestaurantEntity entity) {
        List<String> categoryNames = entity.getCategoryMappings() == null
                ? List.of()
                : entity.getCategoryMappings().stream()
                .map(mapping -> mapping.getCategory().getName())
                .toList();

        return RestaurantResponse.builder()
                .id(entity.getId())
                .ownerId(entity.getOwner().getId())
                .ownerEmail(entity.getOwner().getEmail())
                .name(entity.getName())
                .address(entity.getAddress())
                .area(entity.getArea())
                .phone(entity.getPhone())
                .image(entity.getImage())
                .categoryNames(categoryNames)
                .averageRating(entity.getAverageRating())
                .build();
    }
}
