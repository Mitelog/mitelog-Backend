package kr.co.ync.projectA.domain.member.controller;

import jakarta.validation.Valid;
import kr.co.ync.projectA.domain.bookmark.repository.BookmarkRepository;
import kr.co.ync.projectA.domain.bookmark.service.BookmarkService;
import kr.co.ync.projectA.domain.member.dto.request.MemberLoginRequest;
import kr.co.ync.projectA.domain.member.dto.request.MemberRegisterRequest;
import kr.co.ync.projectA.domain.member.dto.response.MemberPublicResponse;
import kr.co.ync.projectA.domain.member.dto.response.MemberResponse;
import kr.co.ync.projectA.domain.member.dto.response.MemberUpdate;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.member.service.MemberService;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import kr.co.ync.projectA.global.jwt.JwtProvider;
import kr.co.ync.projectA.global.security.MemberSecurity;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://3.36.91.73"
})
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MemberSecurity memberSecurity;
    private final JwtProvider jwtProvider;
    private final BookmarkService bookmarkService;

    /**
     * 회원가입 (비인증 접근 허용)
     * Request : MemberRegisterRequest
     * Response: MemberResponse
     * 201 Created
     */
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody MemberRegisterRequest request) {
        MemberResponse response = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인 (비인증 접근 허용)
     * Request : MemberLoginRequest
     * Response: MemberResponse (JWT 발급 시에는 TokenResponse 등을 사용)
     * 200 OK
     */
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        MemberResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 단건 조회 (인증 불필요 여부는 정책에 따라 조정)
     * Path: /api/members/{id}
     * Response: MemberResponse
     * 200 OK / 404 Not Found
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Long memberId = userDetails.getMember().getId();
        MemberResponse response = memberService.getMember(memberId); // ✅ 바로 받기

        return ResponseEntity.ok(response); // ✅ Optional 처리 불필요
    }

    @GetMapping("/{id}/public")
    public ResponseEntity<ResponseDTO<MemberPublicResponse>> getMemberProfile(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        Long viewerId = null;

        // ✅ JWT 토큰이 있다면 현재 로그인한 사용자 ID 추출
        if (token != null && token.startsWith("Bearer ")) {
            try {
                viewerId = jwtProvider.getUserIdFromToken(token);
            } catch (Exception e) {
                viewerId = null; // 잘못된 토큰이면 비로그인 취급
            }
        }

        // ✅ targetId(프로필 주인)와 viewerId(현재 로그인한 사람) 전달
        MemberPublicResponse response = memberService.getPublicProfile(id, viewerId);
        return ResponseEntity.ok(new ResponseDTO<>(200, "조회 성공", response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ResponseDTO<?>> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody MemberUpdate dto
    ) {
        memberService.updateMember(user.getId(), dto);
        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .status(200)
                        .msg("회원 정보 수정 성공")
                        .build()
        );
    }

    // ✅ 유저 공개용 상세 정보 컨트롤러 확장
    @GetMapping("/{id}/reviews")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDTO<?>> getUserReviews(@PathVariable Long id) {
        var reviews = memberService.getUserReviews(id);
        return ResponseEntity.ok(
                new ResponseDTO<>(200, "리뷰 목록 조회 성공", reviews)
        );
    }

    @GetMapping("/{id}/bookmarks")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDTO<List<RestaurantResponse>>> getUserBookmarks(
            @PathVariable Long id
    ) {
        List<RestaurantResponse> list = bookmarkService.getUserBookmarks(id);
        return ResponseEntity.ok(new ResponseDTO<>(200, "북마크 목록 조회 성공", list));
    }
//    public ResponseEntity<ResponseDTO<?>> getUserBookmarks(@PathVariable Long id) {
//        var bookmarks = memberService.getUserBookmarks(id);
//        return ResponseEntity.ok(
//                new ResponseDTO<>(200, "북마크 목록 조회 성공", bookmarks)
//        );
//    }

    @GetMapping("/{id}/restaurants")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ResponseDTO<?>> getUserRestaurants(@PathVariable Long id) {
        var restaurants = memberService.getUserRestaurants(id);
        return ResponseEntity.ok(
                new ResponseDTO<>(200, "등록 가게 목록 조회 성공", restaurants)
        );
    }



}
