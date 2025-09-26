package kr.co.ync.projectA.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

import kr.co.ync.projectA.domain.member.dto.Member;
import kr.co.ync.projectA.domain.member.exception.MemberNotFoundException;
import kr.co.ync.projectA.domain.member.mapper.MemberMapper;
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
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;

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
        } catch (IllegalArgumentException e) { // null, 빈 문자열
            throw new JwtException("JWT String is Empty", e);
        } catch (WeakKeyException e) { // 알고리즘 예외
            throw new JwtException("Weak Key uesd for signing JWT", e);
        }
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .header()
                .add(Header.JWT_TYPE, JwtType.ACCESS)
                .and()
                .subject(email) // sub
                .issuedAt(new Date(
                        System.currentTimeMillis()
                ))
                .expiration(new Date(
                        System.currentTimeMillis() + jwtProperties.getExpiration()
                ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .header()
                .add(Header.JWT_TYPE, JwtType.REFRESH)
                .and()
                .subject(email) // sub
                .issuedAt(new Date(
                        System.currentTimeMillis()
                ))
                .expiration(new Date(
                        System.currentTimeMillis() + jwtProperties.getRefreshExpiration()
                ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)
        );
    }

    public boolean isWrongType(final Jws<Claims> claims, final JwtType jwtType) {
        return !(claims.getHeader().get(Header.JWT_TYPE).equals(jwtType.toString()));
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = getClaims(token);
        if (isWrongType(claims, JwtType.ACCESS)) {
            throw TokenTypeException.EXCEPTION;
        }
        String email = claims.getPayload().getSubject();

        Member member = memberRepository.findByEmail(email)
                .map(MemberMapper::toDTO)
                .orElseThrow(
                        () -> MemberNotFoundException.EXCEPTION
                );
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        return authentication;
    }
}
