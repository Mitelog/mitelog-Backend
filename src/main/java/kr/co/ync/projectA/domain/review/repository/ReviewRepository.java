package kr.co.ync.projectA.domain.review.repository;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    int countByMember(MemberEntity member);
    List<ReviewEntity> findByRestaurant(RestaurantEntity restaurant);
    List<ReviewEntity> findByMember(MemberEntity member);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.restaurant.id = :restaurantId")
    Double findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);

}
