package kr.co.ync.projectA.domain.member.dto.response;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import lombok.Getter;

@Getter
public class MemberPublicResponse {
    private Long id;
    private String name;
    private int reviewCount;
    private int followerCount;
    private int followingCount;

    public MemberPublicResponse(MemberEntity member, int reviewCount, int followerCount, int followingCount) {
        this.id = member.getId();
        this.name = member.getName();
        this.reviewCount = reviewCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}
