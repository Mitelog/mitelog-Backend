package kr.co.ync.projectA.global.security.config;


import kr.co.ync.projectA.global.jwt.filter.JwtAuthenticationFilter;
import kr.co.ync.projectA.global.jwt.filter.JwtExceptionFilter;
import kr.co.ync.projectA.global.security.handler.JwtAccessDeniedHandler;
import kr.co.ync.projectA.global.security.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/auth/**").permitAll()
                                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                        .anyRequest()
                                        .authenticated()
                )
                .exceptionHandling(
                        handlingconfigurer ->
                                handlingconfigurer
                                        .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                                        .accessDeniedHandler(jwtAccessDeniedHandler())
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler(){
        return new JwtAccessDeniedHandler();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
