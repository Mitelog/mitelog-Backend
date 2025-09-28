package kr.co.ync.projectA.domain.member.repository;

import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testInsertMembers() {
        IntStream.rangeClosed(1, 100)
                .forEach(i -> {
                    String email = "user"+i+"@aaa.com";
                    MemberEntity memberEntity = MemberEntity.builder()
                            .email(email)
                            .password(passwordEncoder.encode("1111"))
                            .name("USER" + i)
                            .role(MemberRole.USER)
                            .phone("0101111222" + i)
                            .build();
                    memberRepository.save(memberEntity);
                });
    }
}