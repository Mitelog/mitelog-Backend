package kr.co.ync.projectA.domain.bookmark.repository;

import kr.co.ync.projectA.domain.bookmark.entity.BookmarkEntity;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    int countByMember(MemberEntity member);

    boolean existsByMemberAndRestaurant(MemberEntity member, RestaurantEntity restaurant);

    Optional<BookmarkEntity> findByMemberAndRestaurant(MemberEntity member, RestaurantEntity restaurant);

    void deleteByMemberAndRestaurant(MemberEntity member, RestaurantEntity restaurant);

    List<BookmarkEntity> findAllByMember(MemberEntity member);

    List<BookmarkEntity> findByMemberId(Long memberId);
}
