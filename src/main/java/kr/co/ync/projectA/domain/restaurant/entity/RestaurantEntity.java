package kr.co.ync.projectA.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;

@Entity
@Table(name = "tbl_restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "owner")
public class RestaurantEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 가게 주인 (Member와 N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity owner;

    // 가게 이름
    @Column(length = 255, nullable = false)
    private String name;

    // 가게 주소
    @Column(length = 255, nullable = false)
    private String address;

    // 지역
    @Column(length = 255, nullable = false)
    private String area;

    // 연락처
    @Column(length = 255, nullable = false)
    private String phone;

    // 대표 이미지
    @Column
    private String image;

    // 사장 이메일 -> MemberEntity에서 가져오는걸로 변경
//    @Column(length = 255, nullable = false, unique = true)
//    private String email;
}
