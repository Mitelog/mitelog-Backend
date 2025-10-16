package kr.co.ync.projectA.domain.auth.controller;

import jakarta.validation.Valid;
import kr.co.ync.projectA.domain.auth.dto.request.AuthenticationRequest;
import kr.co.ync.projectA.domain.auth.dto.request.RefreshTokenRequest;
import kr.co.ync.projectA.domain.auth.dto.response.JsonWebTokenResponse;
import kr.co.ync.projectA.domain.auth.service.AuthService;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://3.36.91.73"
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ResponseDTO<JsonWebTokenResponse>> auth(
            @Valid @RequestBody AuthenticationRequest request
    ){
        JsonWebTokenResponse tokenResponse = authService.auth(request);
        return ResponseEntity.ok(
                ResponseDTO.<JsonWebTokenResponse>builder()
                        .status(200)
                        .msg("로그인 성공")
                        .data(tokenResponse)
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO<JsonWebTokenResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        JsonWebTokenResponse tokenResponse = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(
                ResponseDTO.<JsonWebTokenResponse>builder()
                        .status(200)
                        .msg("토큰 재발급 성공")
                        .data(tokenResponse)
                        .build()
        );
    }
}
