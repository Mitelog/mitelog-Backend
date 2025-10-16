package kr.co.ync.projectA.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import kr.co.ync.projectA.domain.review.dto.request.ReviewRequest;
import kr.co.ync.projectA.domain.review.dto.response.ReviewResponse;
import kr.co.ync.projectA.domain.review.service.ReviewService;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;

import java.util.List;

/**
 * ✅ ReviewController
 * - 리뷰 등록 / 수정 / 삭제 / 조회를 담당
 * - JWT 로그인 기반 (로그인된 사용자만 작성/수정/삭제 가능)
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /* ✅ 리뷰 등록 */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid ReviewRequest request
    ) {
        ReviewResponse response = reviewService.createReview(user.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    /* ✅ 리뷰 수정 */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody @Valid ReviewRequest request
    ) {
        ReviewResponse response = reviewService.updateReview(reviewId, user.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    /* ✅ 리뷰 삭제 */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        reviewService.deleteReview(reviewId, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    /* ✅ 특정 식당의 리뷰 목록 조회 */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRestaurant(
            @PathVariable Long restaurantId
    ) {
        List<ReviewResponse> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    /* ✅ 특정 회원의 리뷰 목록 조회 */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByMember(
            @PathVariable Long memberId
    ) {
        List<ReviewResponse> reviews = reviewService.getReviewsByMemberId(memberId);
        return ResponseEntity.ok(reviews);
    }
}
