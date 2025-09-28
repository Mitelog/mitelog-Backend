package kr.co.ync.projectA.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;

@Entity
@Table(name = "tbl_restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class RestaurantEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 식당 등록자 (Member FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity owner;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 255, nullable = false)
    private String area;  // 나중에 AreaEntity로 분리 가능

    @Column(length = 255, nullable = false)
    private String phone; // unique 제거 고려

    @Column
    private String image; // 단일 이미지 (추후 확장 가능)
}
