package kr.co.ync.projectA.domain.review.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateRequest {
    private String title;
    private String content;
}

