package kr.co.ync.projectA.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ync.projectA.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j //log 사용을 위해
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("🧩 JwtAuthenticationFilter INIT - Path: " + request.getRequestURI());
        String path = request.getRequestURI();

        // ✅ 인증 없이 통과할 경로 (화이트리스트)
        if (path.startsWith("/auth")
                || path.equals("/api/members/register")
                || path.equals("/api/members/register/")) {
            System.out.println("✅ Whitelisted path: " + path);
            filterChain.doFilter(request, response);
            return;
        }
        String token = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            token = authorization.substring(7);

            log.info("🔹 [JWT 필터] Authorization 헤더 감지됨");
            log.info("🔹 토큰: {}", token);
        }

        if (token != null) {
            // ✅ 1. 토큰 유효성 먼저 검증
            if (jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("✅ JWT 인증 성공: {}", authentication.getName());
            } else {
                log.warn("❌ 유효하지 않은 JWT 토큰 - 인증 객체를 설정하지 않음");
            }
        }


        filterChain.doFilter(request, response);
    }
}
