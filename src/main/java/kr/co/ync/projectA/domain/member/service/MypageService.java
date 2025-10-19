package kr.co.ync.projectA.domain.member.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.ync.projectA.domain.follow.Service.FollowService;
import kr.co.ync.projectA.domain.member.dto.response.MypageProfileResponse;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.review.repository.ReviewRepository;
import kr.co.ync.projectA.domain.bookmark.repository.BookmarkRepository;
import kr.co.ync.projectA.domain.reservaiton.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReservationRepository reservationRepository;
    private final FollowService followService;

    public MypageProfileResponse getProfile(MemberEntity member) {
        if (member == null) {
            throw new EntityNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        int reviewCount = reviewRepository.countByMember(member);
        int bookmarkCount = bookmarkRepository.countByMember(member);
        int visitCount = reservationRepository.countByMember(member); // 예약완료된 식당 수 기준

        int followerCount = followService.getFollowerCount(member.getId());
        int followingCount = followService.getFollowingCount(member.getId());

        return MypageProfileResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .reviewCount(1)
//                .reviewCount(reviewCount)
                .visitCount(1)
//                .visitCount(visitCount)
                .bookmarkCount(bookmarkCount)
//                .profileImage(member.getProfileImage())
                .followerCount(followerCount)   // ✅ 추가
                .followingCount(followingCount)
                .build();
    }
}

