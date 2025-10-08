package kr.co.ync.projectA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    private String name;

    private String phone;

    private MemberRole role;
}