package kr.co.ync.projectA.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
