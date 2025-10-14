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
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("🧩 JwtAuthenticationFilter INIT - Path: " + path);

        // ✅ 인증 없이 통과할 경로 (화이트리스트)
        if (path.startsWith("/auth") ||
                path.equals("/api/members/register") ||
                path.equals("/api/members/register/") ||
                path.matches("^/api/members/\\d+/public$") // ✅ 공개 프로필만 허용
        ) {
            System.out.println("✅ Whitelisted path: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 헤더에서 토큰 추출
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;

        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            log.info("🔹 JWT Authorization header detected");
        }

        // ✅ 토큰이 없을 경우 그냥 통과 (permitAll 경로 고려)
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 토큰이 존재하면 유효성 검사 및 인증 처리
        if (jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("✅ JWT 인증 성공: {}", authentication.getName());
        } else {
            log.warn("❌ 유효하지 않은 JWT 토큰 - 인증 객체를 설정하지 않음");
        }

        // ✅ 마지막에 반드시 필터 체인 실행
        filterChain.doFilter(request, response);
    }
}
