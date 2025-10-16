package kr.co.ync.projectA.admin.controller;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;
import kr.co.ync.projectA.domain.review.repository.ReviewRepository;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    /* ======================= 회원 관리 ======================= */

    // 🔍 회원 목록 (페이징 + 항목별 검색)
    @GetMapping("/members")
    public ResponseEntity<?> getMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<MemberEntity> memberPage;

        if (keyword != null && !keyword.isEmpty()) {
            switch (type) {
                case "id" -> {
                    try {
                        Long id = Long.parseLong(keyword);
                        memberPage = memberRepository.findByIdExact(id, pageable);
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body(
                                ResponseDTO.builder().status(400).msg("ID는 숫자여야 합니다.").build());
                    }
                }
                case "name" -> memberPage = memberRepository.findByNameContaining(keyword, pageable);
                case "email" -> memberPage = memberRepository.findByEmailContaining(keyword, pageable);
                case "phone" -> memberPage = memberRepository.findByPhoneContaining(keyword, pageable);
                default -> memberPage = memberRepository.findAll(pageable);
            }
        } else {
            memberPage = memberRepository.findAll(pageable);
        }

        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("회원 목록 조회 성공")
                .data(Map.of(
                        "content", memberPage.getContent(),
                        "totalPages", memberPage.getTotalPages(),
                        "totalElements", memberPage.getTotalElements()
                ))
                .build());
    }

    // 🧾 특정 회원 조회
    @GetMapping("/members/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        return memberRepository.findById(id)
                .map(member -> ResponseEntity.ok(ResponseDTO.builder()
                        .status(200)
                        .msg("회원 상세 조회 성공")
                        .data(member)
                        .build()))
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder().status(404).msg("해당 회원을 찾을 수 없습니다.").build()));
    }

    // ✏️ 회원 수정
    @PutMapping("/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody MemberEntity updated) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setName(updated.getName());
                    member.setPhone(updated.getPhone());
                    memberRepository.save(member);
                    return ResponseEntity.ok(ResponseDTO.builder()
                            .status(200)
                            .msg("회원 정보 수정 성공")
                            .data(member)
                            .build());
                })
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder().status(404).msg("해당 회원을 찾을 수 없습니다.").build()));
    }

    // 🗑 회원 삭제
    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        memberRepository.deleteById(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("회원 삭제 완료")
                .build());
    }

    /* ======================= 식당 관리 ======================= */

    // 🔍 식당 목록 (페이징 + 항목별 검색)
    @GetMapping("/restaurants")
    public ResponseEntity<?> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<RestaurantEntity> restaurantPage;

        if (keyword != null && !keyword.isEmpty()) {
            switch (type) {
                case "id" -> {
                    try {
                        Long id = Long.parseLong(keyword);
                        restaurantPage = restaurantRepository.findByIdExact(id, pageable);
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body(
                                ResponseDTO.builder().status(400).msg("ID는 숫자여야 합니다.").build());
                    }
                }
                case "name" -> restaurantPage = restaurantRepository.findByNameContaining(keyword, pageable);
                case "address" -> restaurantPage = restaurantRepository.findByAddressContaining(keyword, pageable);
                default -> restaurantPage = restaurantRepository.findAll(pageable);
            }
        } else {
            restaurantPage = restaurantRepository.findAll(pageable);
        }

        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("식당 목록 조회 성공")
                .data(Map.of(
                        "content", restaurantPage.getContent(),
                        "totalPages", restaurantPage.getTotalPages(),
                        "totalElements", restaurantPage.getTotalElements()
                ))
                .build());
    }

    // 🧾 특정 식당 조회
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        return restaurantRepository.findById(id)
                .map(res -> ResponseEntity.ok(ResponseDTO.builder()
                        .status(200)
                        .msg("식당 상세 조회 성공")
                        .data(res)
                        .build()))
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder().status(404).msg("해당 식당을 찾을 수 없습니다.").build()));
    }

    // ✏️ 식당 수정
    @PutMapping("/restaurants/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantEntity updated) {
        return restaurantRepository.findById(id)
                .map(res -> {
                    res.setName(updated.getName());
                    res.setAddress(updated.getAddress());
                    restaurantRepository.save(res);
                    return ResponseEntity.ok(ResponseDTO.builder()
                            .status(200)
                            .msg("식당 정보 수정 성공")
                            .data(res)
                            .build());
                })
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder().status(404).msg("해당 식당을 찾을 수 없습니다.").build()));
    }

    // 🗑 식당 삭제
    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        restaurantRepository.deleteById(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("식당 삭제 성공")
                .build());
    }

    /* ======================= 리뷰 관리 ======================= */

    // 🔍 리뷰 목록 (페이징 + 항목별 검색)
    @GetMapping("/reviews")
    public ResponseEntity<?> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<ReviewEntity> reviewPage;

        if (keyword != null && !keyword.isEmpty()) {
            switch (type) {
                case "id" -> {
                    try {
                        Long id = Long.parseLong(keyword);
                        reviewPage = reviewRepository.findByIdExact(id, pageable);
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body(
                                ResponseDTO.builder().status(400).msg("ID는 숫자여야 합니다.").build());
                    }
                }
                case "title" -> reviewPage = reviewRepository.findByTitleContaining(keyword, pageable);
                case "email" -> reviewPage = reviewRepository.findByEmailContaining(keyword, pageable);
                default -> reviewPage = reviewRepository.findAll(pageable);
            }
        } else {
            reviewPage = reviewRepository.findAll(pageable);
        }

        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("리뷰 목록 조회 성공")
                .data(Map.of(
                        "content", reviewPage.getContent(),
                        "totalPages", reviewPage.getTotalPages(),
                        "totalElements", reviewPage.getTotalElements()
                ))
                .build());
    }

    // ✏️ 리뷰 수정
    @PutMapping("/reviews/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewEntity updated) {
        return reviewRepository.findById(id)
                .map(rv -> {
                    rv.setTitle(updated.getTitle());
                    rv.setContent(updated.getContent());
                    reviewRepository.save(rv);
                    return ResponseEntity.ok(ResponseDTO.builder()
                            .status(200)
                            .msg("리뷰 수정 성공")
                            .data(rv)
                            .build());
                })
                .orElse(ResponseEntity.status(404)
                        .body(ResponseDTO.builder().status(404).msg("해당 리뷰를 찾을 수 없습니다.").build()));
    }

    // 🗑 리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewRepository.deleteById(id);
        return ResponseEntity.ok(ResponseDTO.builder()
                .status(200)
                .msg("리뷰 삭제 성공")
                .build());
    }
}
