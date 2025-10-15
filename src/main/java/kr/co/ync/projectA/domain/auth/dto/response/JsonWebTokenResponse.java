package kr.co.ync.projectA.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonWebTokenResponse {

    private String accessToken;
    private String refreshToken;
    private Long memberId; // 프론트에서 로그인 사용자 식별용
}
