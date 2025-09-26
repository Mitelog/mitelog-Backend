package kr.co.ync.projectA.domain.restaurantHours.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

import java.time.LocalTime;

@Entity
@Table(name = "tbl_restaurantHours", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurantId", "dayOfWeek"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class RestaurantHoursEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurantId;

    @Column(nullable = false) //월~일 사이로만 선택 가능 enum 사용할까 생각 중
    private String dayOfWeek;

    @Column
    private LocalTime openTime;

    @Column
    private LocalTime closeTime;

    @Column(nullable = false)
    private Boolean isOpen;

}
