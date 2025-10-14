package kr.co.ync.projectA.domain.follow.mapper;

import kr.co.ync.projectA.domain.follow.dto.FollowResponse;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {

    // 🔹 MemberEntity → FollowResponse 변환
    public FollowResponse toResponse(MemberEntity member) {
        return new FollowResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
