package kr.co.ync.projectA.domain.restaurantHours.entity;

import jakarta.persistence.*;
import kr.co.ync.projectA.domain.restaurantHours.enums.DayOfWeekType;
import lombok.*;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

import java.time.LocalTime;

@Entity
@Table(name = "tbl_restaurant_hours", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "day_of_week"}))
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
    private RestaurantEntity restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "day_of_week") //월~일 사이로만 선택 가능 enum 사용할까 생각 중
    private DayOfWeekType dayOfWeek;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(nullable = false)
    private Boolean isOpen;

}
