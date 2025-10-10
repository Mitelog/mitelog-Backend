package kr.co.ync.projectA.global.security.auth;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;                 // ★ 엔티티
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 스프링 시큐리티가 로그인 시 username(여기선 email)으로 호출하는 메서드.
     * - DTO 매핑 없이, 엔티티 그대로 조회한다.
     * - 찾지 못하면 UsernameNotFoundException을 던져 표준 인증 플로우를 따른다.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // 엔티티를 기반으로 한 Principal(UserDetails) 생성
        return new CustomUserDetails(member);
    }
}
