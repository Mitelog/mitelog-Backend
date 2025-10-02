package kr.co.ync.projectA.domain.auth.controller;

import jakarta.validation.Valid;
import kr.co.ync.projectA.domain.auth.dto.request.AuthenticationRequest;
import kr.co.ync.projectA.domain.auth.dto.request.RefreshTokenRequest;
import kr.co.ync.projectA.domain.auth.dto.response.JsonWebTokenResponse;
import kr.co.ync.projectA.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://3.36.91.73")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<JsonWebTokenResponse> auth(
            @Valid @RequestBody
            AuthenticationRequest request
    ){
        return ResponseEntity.ok(authService.auth(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(
            @Valid @RequestBody RefreshTokenRequest request
            ) {
        return ResponseEntity.ok(authService.refresh(request.getRefreshToken()));
    }

    @PostMapping("signup")
    public ResponseEntity signup(){
        return null;
    }
}
