package kr.co.ync.projectA.domain.review.repository;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    int countByMember(MemberEntity member);
}
