package kr.co.ync.projectA.domain.review.mapper;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.review.dto.request.ReviewRequest;
import kr.co.ync.projectA.domain.review.dto.response.ReviewResponse;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;

public class ReviewMapper {

    /* ✅ Request → Entity */
    public static ReviewEntity toEntity(ReviewRequest dto, MemberEntity member, RestaurantEntity restaurant) {
        return ReviewEntity.builder()
                .restaurant(restaurant)
                .member(member)
                .rating(dto.getRating())
                .title(dto.getTitle())
                .content(dto.getContent())
                .image(dto.getImage())
                .likeNum(0L)
                .build();
    }

    /* ✅ Entity → Response */
    public static ReviewResponse toResponse(ReviewEntity entity) {
        return ReviewResponse.builder()
                .id(entity.getId())
                .restaurantId(entity.getRestaurant().getId())
                .memberId(entity.getMember().getId())
                .memberName(entity.getMember().getName()) // 필요 시 email로 변경 가능
                .rating(entity.getRating())
                .title(entity.getTitle())
                .content(entity.getContent())
                .image(entity.getImage())
                .likeNum(entity.getLikeNum())
                .createdAt(entity.getCreateDateTime())
                .updatedAt(entity.getModifiedDateTime())
                .build();
    }
}
