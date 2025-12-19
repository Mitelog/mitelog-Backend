package kr.co.ync.projectA.admin.dto.response;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminRestaurantRowResponse {
    private Long id;
    private String name;
    private String address;
    private String area;
    private String phone;
    private Double averageRating;

    private OwnerSummary owner;          // password 같은 거 절대 포함 X
    private List<Long> categoryIds;      // restaurant를 다시 담지 말 것!

    @Getter
    @Builder
    public static class OwnerSummary {
        private Long id;
        private String email;
        private String name;
        private String role;
    }

    public static AdminRestaurantRowResponse from(RestaurantEntity e) {
        return AdminRestaurantRowResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .address(e.getAddress())
                .area(e.getArea())
                .phone(e.getPhone())
                .averageRating(e.getAverageRating())
                .owner(OwnerSummary.builder()
                        .id(e.getOwner() != null ? e.getOwner().getId() : null)
                        .email(e.getOwner() != null ? e.getOwner().getEmail() : null)
                        .name(e.getOwner() != null ? e.getOwner().getName() : null)
                        .role(e.getOwner() != null ? String.valueOf(e.getOwner().getRole()) : null)
                        .build()
                )
                .categoryIds(
                        e.getCategoryMappings() == null ? List.of()
                                : e.getCategoryMappings().stream()
                                .map(m -> m.getCategory() != null ? m.getCategory().getId() : null)
                                .filter(id -> id != null)
                                .toList()
                )
                .build();
    }
}
