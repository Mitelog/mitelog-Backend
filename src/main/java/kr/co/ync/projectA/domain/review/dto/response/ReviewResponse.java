package kr.co.ync.projectA.domain.review.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Long restaurantId;
    private Long memberId;
    private String memberName;   // or email, nickname 등
    private int rating;
    private String title;
    private String content;
    private String image;
    private Long likeNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
