package kr.co.ync.projectA.global.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class InitConfig {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminUser() {
        // admin 계정이 없을 때만 생성
        if (memberRepository.findByEmail("admin@test.com").isEmpty()) {
            MemberEntity admin = MemberEntity.builder()
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("1234")) // BCrypt 암호화
                    .name("관리자")
                    .phone("01000000000")
                    .role(MemberRole.ADMIN)
                    .build();
            memberRepository.save(admin);
        }
    }
}
