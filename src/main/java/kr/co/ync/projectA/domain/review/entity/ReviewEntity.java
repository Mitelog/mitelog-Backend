package kr.co.ync.projectA.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;

@Entity
@Table(name = "tbl_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class ReviewEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)//fk //이메일 어노테이션도 있다 다시 생각해보기
    private MemberEntity email;

    @Column(nullable = false)
    private Long rating; //1~5 사이 정수값만 받을거임

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String image;

    @Column(nullable = false)
    private Long likeNum = 0L; //디폴트 값 이렇게 주는게 맞는지 물어보기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;
}
