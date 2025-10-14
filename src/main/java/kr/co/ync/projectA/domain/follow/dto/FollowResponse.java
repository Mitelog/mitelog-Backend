package kr.co.ync.projectA.domain.follow.dto;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowResponse {
    private Long memberId;
    private String name;
    private String email;

    public static FollowResponse from(MemberEntity member) {
        return new FollowResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
