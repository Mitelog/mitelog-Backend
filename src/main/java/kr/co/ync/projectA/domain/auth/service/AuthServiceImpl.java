package kr.co.ync.projectA.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.ync.projectA.domain.auth.dto.request.AuthenticationRequest;
import kr.co.ync.projectA.domain.auth.dto.response.JsonWebTokenResponse;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;      // ✅ 엔티티 사용
import kr.co.ync.projectA.global.jwt.JwtProvider;
import kr.co.ync.projectA.global.jwt.enums.JwtType;
import kr.co.ync.projectA.global.jwt.exception.TokenTypeException;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager; // 로그인 시도 처리 (Password 검증)
    private final JwtProvider jwtProvider;                      // 액세스/리프레시 토큰 생성·검증

    /**
     * 📌 이메일/비밀번호로 인증 → Access/Refresh 발급
     * - AuthenticationManager가 내부적으로 UserDetailsService를 호출하여 사용자 인증
     * - Principal은 CustomUserDetails → 그 안의 MemberEntity에서 이메일 꺼내 토큰 생성
     */
    @Override
    public JsonWebTokenResponse auth(AuthenticationRequest request) {
        // 스프링 시큐리티 표준 로그인 토큰 (id, pw)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 인증 성공 시 Principal 은 CustomUserDetails
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        // ✅ DTO 아님: 엔티티 기반으로 안전하게 사용
        MemberEntity member = principal.getMember();

        // 이메일을 sub 로 하여 Access/Refresh 생성 (필요시 memberId/roles 클레임 추가 가능)
        return JsonWebTokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(member.getEmail()))
                .refreshToken(jwtProvider.generateRefreshToken(member.getEmail()))
                .build();
    }

    /**
     * 📌 Refresh 토큰으로 Access 재발급
     * - 헤더의 토큰이 Refresh 타입인지 검증 (ACCESS 와 혼용 방지)
     * - sub(email)에서 다시 Access 토큰 생성
     */
    @Override
    public JsonWebTokenResponse refresh(String token) {
        // 서명/구조 검증 + Claims 추출 (예외는 JwtProvider에서 일괄 변환)
        Jws<Claims> claims = jwtProvider.getClaims(token);

        // Refresh 토큰만 허용 (헤더 JWT_TYPE 검사)
        if (jwtProvider.isWrongType(claims, JwtType.REFRESH)) {
            throw TokenTypeException.EXCEPTION;
        }

        // sub(email) 추출 후 Access 재발급
        String email = claims.getPayload().getSubject();

        return JsonWebTokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(email))
                .build();
    }
}
