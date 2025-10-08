package kr.co.ync.projectA.domain.member.dto;

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

    private String password;

    private String name;

    private String phone;

    private MemberRole role;
}