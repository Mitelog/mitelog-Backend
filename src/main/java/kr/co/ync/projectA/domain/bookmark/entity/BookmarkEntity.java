package kr.co.ync.projectA.domain.bookmark.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

@Entity
@Table(name = "tbl_bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class BookmarkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ 식당 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    // ✅ 회원 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

}
