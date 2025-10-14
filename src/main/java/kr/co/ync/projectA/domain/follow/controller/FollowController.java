package kr.co.ync.projectA.domain.follow.controller;

import kr.co.ync.projectA.domain.follow.Service.FollowService;
import kr.co.ync.projectA.domain.follow.dto.FollowResponse;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import kr.co.ync.projectA.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;
    private final JwtProvider jwtProvider; // 토큰에서 ID 추출용

    @PostMapping("/{targetId}")
    public ResponseEntity<ResponseDTO<Void>> follow(
            @PathVariable Long targetId,
            @RequestHeader("Authorization") String token) {
        Long memberId = jwtProvider.getUserIdFromToken(token);
        followService.follow(memberId, targetId);
        return ResponseEntity.ok(new ResponseDTO<>(200, "팔로우 성공", null));
    }

    @DeleteMapping("/{targetId}")
    public ResponseEntity<ResponseDTO<Void>> unfollow(
            @PathVariable Long targetId,
            @RequestHeader("Authorization") String token) {
        Long memberId = jwtProvider.getUserIdFromToken(token);
        followService.unfollow(memberId, targetId);
        return ResponseEntity.ok(new ResponseDTO<>(200, "언팔로우 성공", null));
    }

    // ✅ 팔로잉 목록
    @GetMapping("/following/{memberId}")
    public ResponseEntity<ResponseDTO<List<FollowResponse>>> getFollowingList(@PathVariable Long memberId) {
        List<FollowResponse> list = followService.getFollowingList(memberId);
        return ResponseEntity.ok(new ResponseDTO<>(200, "팔로잉 목록 조회 성공", list));
    }

    // ✅ 팔로워 목록
    @GetMapping("/followers/{memberId}")
    public ResponseEntity<ResponseDTO<List<FollowResponse>>> getFollowerList(@PathVariable Long memberId) {
        List<FollowResponse> list = followService.getFollowerList(memberId);
        return ResponseEntity.ok(new ResponseDTO<>(200, "팔로워 목록 조회 성공", list));
    }
}
