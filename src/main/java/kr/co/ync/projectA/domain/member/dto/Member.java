package kr.co.ync.projectA.domain.member.dto;

import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Member {
<<<<<<< HEAD
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
=======

    private Long id;

    private String email;

    private String password;

    private String name;

    private String phone;

>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)
    private MemberRole role;
}
