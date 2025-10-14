package kr.co.ync.projectA.domain.follow.reposiroty;

import kr.co.ync.projectA.domain.follow.entity.FollowEntity;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    boolean existsByFollowerAndFollowing(MemberEntity follower, MemberEntity following);
    void deleteByFollowerAndFollowing(MemberEntity follower, MemberEntity following);

    List<FollowEntity> findByFollower(MemberEntity follower);   // 내가 팔로우한 목록
    List<FollowEntity> findByFollowing(MemberEntity following); // 나를 팔로우한 목록

    int countByFollowerId(Long followerId);   // 내가 팔로우한 사람 수
    int countByFollowingId(Long followingId); // 나를 팔로우한 사람 수
}
