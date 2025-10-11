package kr.co.ync.projectA.domain.member.controller;

import jakarta.validation.Valid;
import kr.co.ync.projectA.domain.member.dto.request.MemberLoginRequest;
import kr.co.ync.projectA.domain.member.dto.request.MemberRegisterRequest;
import kr.co.ync.projectA.domain.member.dto.response.MemberResponse;
import kr.co.ync.projectA.domain.member.service.MemberService;
import kr.co.ync.projectA.global.security.MemberSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://3.36.91.73"
})
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberSecurity memberSecurity;

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
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {
        Optional<MemberResponse> result = memberService.getMember(id);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
