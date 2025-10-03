package kr.co.ync.projectA.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ync.projectA.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
            ServletException, IOException {

        String path = request.getRequestURI();
        //예외 경로는 검사안하고 바로 return
        if (path.startsWith("/auth") || path.equals("/api/members/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(
                StringUtils.hasText(authorization) && authorization.startsWith("Bearer")
        ){
            token = authorization.substring(7);
        }

        if(token != null){
            Authentication authentication = jwtProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);
    }
}
