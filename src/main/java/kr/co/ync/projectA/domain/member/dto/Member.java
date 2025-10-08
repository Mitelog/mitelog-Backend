package kr.co.ync.projectA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @Builder.Default
    private MemberRole role = MemberRole.USER;
}