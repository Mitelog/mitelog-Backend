package kr.co.ync.projectA.global.security;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 📌 MemberSecurity
 * - 현재 로그인한 사용자의 정보를 SecurityContext에서 꺼내는 헬퍼 클래스.
 * - 서비스나 컨트롤러에서 중복되는 인증 조회 코드를 단순화한다.
 */
@Component
public class MemberSecurity {

    /**
     * ✅ 현재 로그인한 사용자의 MemberEntity를 반환한다.
     * - 로그인되어 있지 않으면 AccessDeniedException 발생.
     * - 주로 "로그인한 사용자 본인 정보"가 필요한 서비스/컨트롤러에서 사용.
     */
    public MemberEntity getCurrentMemberOrThrow() {

        // 현재 SecurityContext(시큐리티 세션)에 저장된 인증 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 객체가 없거나(로그인 안 함), 익명 사용자면 접근 차단
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("로그인이 필요한 요청입니다.");
        }

        // 인증된 사용자 정보(Principal)를 CustomUserDetails로 캐스팅
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // CustomUserDetails 안에는 MemberEntity가 포함되어 있음 → 그대로 반환
        return userDetails.getMember();
    }
}
