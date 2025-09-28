package kr.co.ync.projectA.domain.auth.service;

import kr.co.ync.projectA.domain.auth.dto.request.AuthenticationRequest;
import kr.co.ync.projectA.domain.auth.dto.response.JsonWebTokenResponse;

public interface AuthService {

    JsonWebTokenResponse auth(AuthenticationRequest request);

    JsonWebTokenResponse refresh(String token);
}
