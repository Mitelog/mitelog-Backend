package kr.co.ync.projectA.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Member {

    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    private String name;

    private String phone;

    private MemberRole role;
}