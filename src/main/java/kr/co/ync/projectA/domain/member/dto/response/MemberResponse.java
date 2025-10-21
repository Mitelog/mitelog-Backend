package kr.co.ync.projectA.domain.member.dto.response;

import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

/**
 * 응답 DTO
 */

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String email;
    private String name;
    private String phone;
    private MemberRole role;

    // ✅ 마이페이지용 카운트 필드들 추가
    private int reviewCount;      // 리뷰 개수
    private int bookmarkCount;    // 북마크 개수
    private int restaurantCount;  // 등록한 가게 개수
    private int followerCount;    // 팔로워 수
    private int followingCount;   // 팔로잉 수
}
