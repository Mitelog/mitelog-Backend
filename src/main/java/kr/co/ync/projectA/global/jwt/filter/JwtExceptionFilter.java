package kr.co.ync.projectA.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ 화이트리스트: JWT 예외 발생 시 401 반환하지 않음
        if (path.startsWith("/auth")
                || path.equals("/api/members/register")
                || path.equals("/api/members/register/")
                || path.startsWith("/api/restaurants")
                || path.startsWith("/api/menus")
                || path.startsWith("/api/categories")
                || path.startsWith("/api/reviews/restaurant") // ✅ 리뷰 조회 공개 허용
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // ✅ JWT 오류가 발생해도 공개 경로는 401 반환 안 함
            if (isPublicPath(path)) {
                System.out.println("⚠️ JwtException 무시 (공개 요청): " + path);
                filterChain.doFilter(request, response);
                return;
            }

            // ✅ 나머지 요청은 401 반환
            writeError(response, e.getMessage());
        }
    }

    /** ✅ 공개 경로 여부 검사 */
    private boolean isPublicPath(String path) {
        return path.startsWith("/api/reviews/restaurant")
                || path.startsWith("/api/restaurants")
                || path.startsWith("/api/menus")
                || path.startsWith("/api/categories");
    }

    private void writeError(HttpServletResponse response, String message) throws IOException {
        if (response.isCommitted()) return;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = Map.of(
                "status", 401,
                "message", message
        );

        objectMapper.writeValue(response.getWriter(), body);
    }
}
