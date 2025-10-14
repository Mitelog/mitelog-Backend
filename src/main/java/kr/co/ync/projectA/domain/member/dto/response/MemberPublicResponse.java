package kr.co.ync.projectA.domain.member.dto.response;

import lombok.*;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import lombok.Getter;

@Getter
public class MemberPublicResponse {
    private Long id;
    private String name;
    private int reviewCount;
    private int followerCount;
    private int followingCount;
    private boolean followed; // ✅ 이름을 'isFollowed'로 지정

    public MemberPublicResponse(MemberEntity member, int reviewCount, int followerCount, int followingCount, boolean followed) {
        this.id = member.getId();
        this.name = member.getName();
        this.reviewCount = reviewCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.followed = followed;
    }
}


