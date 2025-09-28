package kr.co.ync.projectA.domain.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.co.ync.projectA.domain.auth.dto.request.AuthenticationRequest;
import kr.co.ync.projectA.domain.auth.dto.response.JsonWebTokenResponse;
import kr.co.ync.projectA.domain.member.dto.Member;
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
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public JsonWebTokenResponse auth(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        Member member = ((CustomUserDetails)authentication.getPrincipal()).getMember();
        return JsonWebTokenResponse.builder()
                .accessToken(jwtProvider.generateAccessToken(member.getEmail()))
                .refreshToken(jwtProvider.generateRefreshToken(member.getEmail())).build();
    }

    @Override
    public JsonWebTokenResponse refresh(String token) {
        Jws<Claims> claims = jwtProvider.getClaims(token);
        if(jwtProvider.isWrongType(claims, JwtType.REFRESH)){
            throw TokenTypeException.EXCEPTION;
        }
        String email = claims.getPayload().getSubject();

        return JsonWebTokenResponse.builder().accessToken(jwtProvider.generateAccessToken(email)).build();
    }
}
