package kr.co.ync.projectA.domain.review.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    /* ✅ 어떤 식당에 대한 리뷰인지 */
    @NotNull(message = "식당 ID는 필수입니다.")
    private Long restaurantId;

    /* ✅ 평점 (1~5 정수) */
    @Min(value = 1, message = "평점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점 이하이어야 합니다.")
    private int rating;

    /* ✅ 리뷰 제목 */
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    /* ✅ 리뷰 내용 */
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    /* ✅ 리뷰 이미지 (선택 사항) */
    private String image;
}
