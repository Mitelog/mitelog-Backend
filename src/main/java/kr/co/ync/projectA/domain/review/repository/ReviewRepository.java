package kr.co.ync.projectA.domain.review.repository;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    /* ================= 일반 사용자용 ================= */

    int countByMember(MemberEntity member);

    // ✅ 필드명이 restaurantId니까, 여기 이름도 restaurantId로
    List<ReviewEntity> findByRestaurantId(RestaurantEntity restaurantId);

    List<ReviewEntity> findByMember(MemberEntity member);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.restaurantId.id = :restaurantId")
    Double findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);


    /* ================= 관리자 페이지용 ================= */

    Page<ReviewEntity> findByTitleContaining(String title, Pageable pageable);

    // ✅ email 필드 대신 member를 통해 email 접근 가능하도록 Query로 처리
    @Query("SELECT r FROM ReviewEntity r WHERE r.member.email LIKE %:email%")
    Page<ReviewEntity> findByEmailContaining(@Param("email") String email, Pageable pageable);

    @Query("SELECT r FROM ReviewEntity r WHERE r.id = :id")
    Page<ReviewEntity> findByIdExact(@Param("id") Long id, Pageable pageable);
}
