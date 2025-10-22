package kr.co.ync.projectA.domain.review.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "tbl_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ReviewEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 리뷰 대상 가게 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private RestaurantEntity restaurant;

    /* 리뷰 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonBackReference
    private MemberEntity member;

    /* 평점 (1~5 정수) */
    @Column(nullable = false)
    private int rating;

    /* 리뷰 제목 */
    @Column(nullable = false)
    private String title;

    /* 리뷰 내용 */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /* 이미지 경로 */
    @Column
    private String image;

    /* 좋아요 수 (기본값 0) */
    @Column(nullable = false)
    @ColumnDefault("0")
    private Long likeNum;
}
