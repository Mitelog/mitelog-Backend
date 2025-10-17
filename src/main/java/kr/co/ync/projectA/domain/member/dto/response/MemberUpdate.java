package kr.co.ync.projectA.domain.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdate {
    private String name;
    private String phone;
    private String password;
}
