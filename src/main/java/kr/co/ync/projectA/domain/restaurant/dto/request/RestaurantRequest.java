package kr.co.ync.projectA.domain.restaurant.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ✅ 식당 등록 및 수정 요청 DTO
 * 프론트엔드에서 전달받은 식당 정보 + 카테고리 ID 목록을 포함
 */
@Getter
@Setter
public class RestaurantRequest {

    private String name;
    private String address;
    private String area;
    private String phone;
    private String description;
    private String image; // (이미지 필드 이미 있으면 유지)

    /** ✅ 선택된 카테고리 ID 목록 */
    private List<Long> categoryIds;
}
