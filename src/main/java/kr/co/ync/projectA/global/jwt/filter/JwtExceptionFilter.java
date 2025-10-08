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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //전처리
        String path = request.getRequestURI();
        
        if (path.startsWith("/auth") || path.equals("/api/members/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            filterChain.doFilter(request, response);
        } catch (JwtException e){
            writeError(response, e.getMessage());
        }


        //후처리
    }

    private void writeError(HttpServletResponse response, String message) throws IOException{
        if(response.isCommitted()) return;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = Map.of("status", 401, "message", message);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
