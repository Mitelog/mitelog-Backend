package kr.co.ync.projectA.domain.follow.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import kr.co.ync.projectA.domain.follow.dto.FollowResponse;
import kr.co.ync.projectA.domain.follow.entity.FollowEntity;
import kr.co.ync.projectA.domain.follow.mapper.FollowMapper;
import kr.co.ync.projectA.domain.follow.reposiroty.FollowRepository;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final FollowMapper followMapper;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }

        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("팔로워를 찾을 수 없습니다."));
        MemberEntity following = memberRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("대상 사용자를 찾을 수 없습니다."));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }

        followRepository.save(FollowEntity.builder()
                .follower(follower)
                .following(following)
                .build());
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        MemberEntity follower = memberRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("팔로워를 찾을 수 없습니다."));
        MemberEntity following = memberRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("대상 사용자를 찾을 수 없습니다."));

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }


    // 팔로잉 목록 (내가 팔로우한 사람들)
    public List<FollowResponse> getFollowingList(Long memberId) {
        MemberEntity follower = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return followRepository.findByFollower(follower)
                .stream()
                .map(follow -> followMapper.toResponse(follow.getFollowing()))
                .toList();
    }

    // 팔로워 목록 (나를 팔로우하는 사람들)
    public List<FollowResponse> getFollowerList(Long memberId) {
        MemberEntity following = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        return followRepository.findByFollowing(following)
                .stream()
                .map(follow -> followMapper.toResponse(follow.getFollower()))
                .toList();
    }

    // ✅ 나를 팔로우하는 사람 수 (팔로워 수)
    public int getFollowerCount(Long memberId) {
        return followRepository.countByFollowingId(memberId);
    }

    // ✅ 내가 팔로우 중인 사람 수 (팔로잉 수)
    public int getFollowingCount(Long memberId) {
        return followRepository.countByFollowerId(memberId);
    }
}
