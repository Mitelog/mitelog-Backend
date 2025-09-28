package kr.co.ync.projectA.domain.member.controller;

import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody Member member) {
        Member saved = memberService.register(member);
        return ResponseEntity.ok(saved);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestParam String email,
                                        @RequestParam String password) {
        Member loggedIn = memberService.login(email, password);
        return ResponseEntity.ok(loggedIn);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        return memberService.getMember(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
