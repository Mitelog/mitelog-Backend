package kr.co.ync.projectA.domain.member.controller;

import kr.co.ync.projectA.domain.member.dto.response.MypageProfileResponse;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.service.MypageService;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO<MypageProfileResponse>> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberEntity member = userDetails.getMember();
        return ResponseEntity.ok(
                new ResponseDTO<>(200, "조회 성공", mypageService.getProfile(member))
        );
    }
}
