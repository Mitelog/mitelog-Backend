package kr.co.ync.projectA.global.security.auth;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    // 📌 인증 주체(Principal)로 사용할 최소 데이터는 보통 엔티티에서 가져온다.
    //    여기서는 MemberEntity 자체를 들고 다니되, 외부로 노출은 최소화(게터 제공)하는 패턴.
    private final MemberEntity member;

    // 📌 스프링 시큐리티가 요구하는 권한(ROLE_*) 목록
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(MemberEntity member) {
        this.member = member;
        // 📌 Enum MemberRole → "ROLE_USER" 같은 문자열로 변환해서 권한 등록
        //    member.getRole().getKey()가 "ROLE_USER" 형식이어야 한다.
        this.authorities = Collections.singleton(
                new SimpleGrantedAuthority(member.getRole().getKey())
        );
    }

    // 📌 현재 사용자의 권한 컬렉션 반환 (접근 제어에 사용)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 📌 인증 시 내부 비교에 쓰이는 비밀번호 (반드시 암호화 저장)
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 📌 스프링 시큐리티의 "username" 개념. 여기서는 이메일을 사용자명으로 사용.
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    // 📌 아래 4개는 계정 상태 플래그. 별도 컬럼이 없으면 true 고정.
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
