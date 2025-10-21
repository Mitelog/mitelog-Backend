package kr.co.ync.projectA.domain.member.dto.response;

import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypageProfileResponse {//안씀 분리 귀찮음

    private Long id;             // 회원 고유 ID
    private String email;        // 이메일
    private String name;         // 이름
    private String phone;
    private MemberRole role;

    private int reviewCount;     // 내가 쓴 리뷰 수
    private int restaurantCount;      // 방문한 식당 수
    private int bookmarkCount;   // 북마크한 식당 수
    private int followerCount;   // ✅ 추가
    private int followingCount;  // ✅ 추가
    private String profileImage; // 프로필 이미지 URL
}
