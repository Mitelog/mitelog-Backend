package kr.co.ync.projectA.domain.reservaiton.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;

import java.time.LocalDateTime;
@Entity
@Table(name = "tbl_reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class ReservationEntity extends BaseTimeEntity {

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
    private LocalDateTime visit;

    @Column(length = 255, nullable = false)
    private Long numPeople;
}
