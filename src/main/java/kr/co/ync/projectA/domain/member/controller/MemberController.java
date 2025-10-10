package kr.co.ync.projectA.domain.member.controller;

import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.domain.member.service.MemberService;
import kr.co.ync.projectA.global.security.MemberSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://3.36.91.73")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberSecurity memberSecurity;

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody Member member) {
        Member saved = memberService.register(member);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        return memberService.getMember(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<Member> getMyInfo() {
        Member member = memberSecurity.getMember(); // 🔹 현재 로그인한 사용자 정보

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 안 된 경우
        }

        return ResponseEntity.ok(member);
    }
}
