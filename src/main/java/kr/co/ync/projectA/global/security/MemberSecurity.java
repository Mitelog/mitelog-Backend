package kr.co.ync.projectA.global.security;



import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MemberSecurity {
    public Member getMember(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication();

        return userDetails.getMember();
    }
}
