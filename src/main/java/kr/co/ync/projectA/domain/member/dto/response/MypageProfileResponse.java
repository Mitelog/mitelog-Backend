package kr.co.ync.projectA.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MypageProfileResponse {

    private Long id;             // 회원 고유 ID
    private String email;        // 이메일
    private String name;         // 이름
    private int reviewCount;     // 내가 쓴 리뷰 수
    private int visitCount;      // 방문한 식당 수
    private int bookmarkCount;   // 북마크한 식당 수
    private int followerCount;   // ✅ 추가
    private int followingCount;  // ✅ 추가
    private String profileImage; // 프로필 이미지 URL
}
