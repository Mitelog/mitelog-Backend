package kr.co.ync.projectA.domain.member.dto.response;

import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

/**
 * 응답 DTO
 */

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private MemberRole role;
}
