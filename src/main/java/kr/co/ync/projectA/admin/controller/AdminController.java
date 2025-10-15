package kr.co.ync.projectA.admin.controller;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;
import kr.co.ync.projectA.domain.review.repository.ReviewRepository;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    /* ------------------------ 회원 관리 ------------------------ */

    // 전체 회원 조회
    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers() {
        List<MemberEntity> list = memberRepository.findAll();
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("전체 회원 조회 성공")
                .data(list)
                .build());
    }

    // 특정 회원 조회
    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        return memberRepository.findById(id)
                .map(member -> ResponseEntity.ok(ResponseDTO.builder()
                        .status(200)
                        .msg("회원 상세 조회 성공")
                        .data(member)
                        .build()))
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder()
                                .status(404)
                                .msg("해당 회원을 찾을 수 없습니다.")
                                .build()));
    }

    // 회원 삭제
    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("회원 삭제 완료")
                .build());
    }

    /* ------------------------ 식당 관리 ------------------------ */

    // 전체 식당 조회
    @GetMapping("/restaurants")
    public ResponseEntity<?> getAllRestaurants() {
        List<RestaurantEntity> list = restaurantRepository.findAll();
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("전체 식당 조회 성공")
                .data(list)
                .build());
    }

    // 특정 식당 조회
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .map(res -> ResponseEntity.ok(ResponseDTO.builder()
                        .status(200)
                        .msg("식당 상세 조회 성공")
                        .data(res)
                        .build()))
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder()
                                .status(404)
                                .msg("해당 식당을 찾을 수 없습니다.")
                                .build()));
    }

    // 식당 삭제
    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        restaurantRepository.deleteById(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("식당 삭제 성공")
                .build());
    }

    /* ------------------------ 리뷰 관리 ------------------------ */

    // 전체 리뷰 조회
    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviews() {
        List<ReviewEntity> list = reviewRepository.findAll();
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("전체 리뷰 조회 성공")
                .data(list)
                .build());
    }

    // 특정 리뷰 조회
    @GetMapping("/reviews/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .map(review -> ResponseEntity.ok(ResponseDTO.builder()
                        .status(200)
                        .msg("리뷰 상세 조회 성공")
                        .data(review)
                        .build()))
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder()
                                .status(404)
                                .msg("해당 리뷰를 찾을 수 없습니다.")
                                .build()));
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewRepository.deleteById(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("리뷰 삭제 성공")
                .build());
    }
}
