package kr.co.ync.projectA.domain.restaurant.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
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
    private Double averageRating;
    private List<String> categoryNames;

    public static RestaurantResponse fromEntity(RestaurantEntity entity) {
        return RestaurantResponse.builder()
                .id(entity.getId())
                .ownerId(entity.getOwner().getId())
                .ownerEmail(entity.getOwner().getEmail())
                .name(entity.getName())
                .address(entity.getAddress())
                .area(entity.getArea())
                .phone(entity.getPhone())
                .image(entity.getImage())
                .averageRating(entity.getAverageRating())
                .categoryNames(
                        entity.getCategoryMappings()
                                .stream()
                                .map(mapping -> mapping.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
