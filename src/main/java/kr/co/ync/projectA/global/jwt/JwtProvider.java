package kr.co.ync.projectA.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.annotation.Nullable;
import kr.co.ync.projectA.domain.member.entity.MemberEntity; // ★ 엔티티로 고정
import kr.co.ync.projectA.domain.member.exception.MemberNotFoundException;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.global.jwt.enums.JwtType;
import kr.co.ync.projectA.global.jwt.exception.TokenTypeException;
import kr.co.ync.projectA.global.jwt.properties.JwtProperties;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    // 📌 외부 설정(application.yml 등)에서 주입하는 토큰 옵션(시크릿, 만료시간 등)
    private final JwtProperties jwtProperties;

    // 📌 토큰에서 이메일(sub) 뽑은 뒤, DB에서 사용자를 조회하기 위해 사용
    private final MemberRepository memberRepository;

    /**
     * 📌 토큰을 파싱해서 Jws<Claims>로 반환.
     *    - 여기서 서명 검증(비밀키 일치) & 구조검증을 같이 수행한다.
     *    - 다양한 예외를 명시적으로 캐치해 주면 디버깅/로깅에 유리.
     */
    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) { // 토큰 만료
            throw new JwtException("Expired JWT", e);
        } catch (UnsupportedJwtException e) { // 지원하지 않는 JWT
            throw new JwtException("Unsupported JWT", e);
        } catch (MalformedJwtException e) { // 구조가 잘못된 JWT
            throw new JwtException("Malformed JWT", e);
        } catch (SignatureException e) { // 서명이 잘못된 JWT
            throw new JwtException("Invalid JWT", e);
        } catch (IllegalArgumentException e) { // null/빈 문자열 등
            throw new JwtException("JWT String is Empty", e);
        } catch (WeakKeyException e) { // 서명 키가 너무 약함
            throw new JwtException("Weak Key used for signing JWT", e);
        } catch (JwtException e) {
            System.out.println("❌ JWT 파싱 실패: " + e.getMessage());
            throw e;
        }

    }

    /**
     * 📌 Access 토큰 생성
     *    - subject(sub)에 "이메일"을 넣음(= 인증 식별자)
     *    - 필요하다면 .claim("mid", memberId) 처럼 최소 클레임을 추가하는 것도 가능
     */
    public String generateAccessToken(String email) {
        // DB에서 회원 정보 조회
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .add("token_type", JwtType.ACCESS)
                .add(Header.JWT_TYPE, JwtType.ACCESS)
                .and()
                .subject(email)
                // ✅ 여기서 role 추가
                .claim("role", member.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 📌 Refresh 토큰 생성
     *    - 보통 Access보다 만료시간이 길고, 재발급 용도로만 사용
     */
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .header()
                .add(Header.JWT_TYPE, JwtType.REFRESH)
                .and()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 📌 서명용 SecretKey 생성
     *    - HS256 기준으로 충분히 긴 base64 문자열을 권장
     *    - application.yml: jwt.secretKey: "base64-encoded-very-long-secret..."
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 📌 토큰 헤더에 저장한 타입(ACCESS/REFRESH) 일치 확인
     *    - 재발급/인가 과정에서 실수로 잘못된 토큰을 쓰는 걸 방지
     */
    public boolean isWrongType(final Jws<Claims> claims, final JwtType jwtType) {
        return !(claims.getHeader().get(Header.JWT_TYPE).equals(jwtType.toString()));
    }

    /**
     * 📌 토큰을 Authentication(스프링 시큐리티 표준 인증객체)로 변환
     *    절차:
     *    1) 토큰 파싱 → Claims
     *    2) 타입 검증 (ACCESS 여야 한다)
     *    3) subject(=email) 추출
     *    4) DB에서 사용자 조회(엔티티 기준) — ❗ DTO로 매핑하지 않음
     *    5) CustomUserDetails 생성
     *    6) UsernamePasswordAuthenticationToken으로 감싸 반환(SecurityContext에 들어갈 타입)
     */
    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = getClaims(token);

        if (isWrongType(claims, JwtType.ACCESS)) {
            // ex) Refresh 토큰으로 API를 호출하는 실수를 차단
            throw TokenTypeException.EXCEPTION;
        }

        String email = claims.getPayload().getSubject();

        // ✅ 엔티티로 직접 조회 (DTO 매핑 X)
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        CustomUserDetails principal = new CustomUserDetails(member);

        // 📌 credentials(null)은 패스워드 재검증 안 함을 의미. 권한은 principal에서 가져옴.
        return new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );
    }

    /**
     * 📌 JWT 유효성 검증
     *  - 토큰이 만료되었거나 서명이 올바르지 않으면 false 반환
     *  - 유효할 경우 true 반환
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token); // 내부적으로 Jwts.parser()를 실행해 서명과 만료 여부 검증
            return true;
        } catch (JwtException e) {
            System.out.println("❌ JWT 검증 실패: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 잘못된 JWT 토큰 형식: " + e.getMessage());
            return false;
        }
    }


    // (선택) 토큰에서 이메일만 뽑고 싶을 때 간단 유틸
    @Nullable
    public String extractEmailOrNull(String token) {
        try {
            return getClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * 📌 Authorization 헤더에서 회원 ID를 추출
     *  - "Bearer " 접두사 제거 후 실제 토큰 파싱
     *  - 토큰에서 이메일(subject) 꺼내고 DB에서 memberId 반환
     */
    public Long getMemberIdFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new JwtException("Invalid Authorization header");
        }
        String pureToken = token.substring(7); // "Bearer " 제거
        String email = extractEmailOrNull(pureToken);
        if (email == null) {
            throw new JwtException("Invalid token or cannot extract email");
        }

        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        return member.getId();
    }

    /**
     * 📌 JWT 토큰으로부터 회원 ID를 추출
     *  - Authorization 헤더 형식 ("Bearer ") 포함 시 자동 제거
     *  - subject(email)을 꺼내서 DB 조회 후 memberId 반환
     */
    public Long getUserIdFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new JwtException("Token is null or empty");
        }

        // "Bearer " 접두사 제거 (있을 경우)
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = extractEmailOrNull(token);
        if (email == null) {
            throw new JwtException("Invalid token: cannot extract email");
        }

        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);

        return member.getId();
    }


}
