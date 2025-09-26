package kr.co.ync.projectA.domain.restaurantHoildays.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "tbl_restaurantHolidays")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class RestaurantHolidaysEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurantId;

    @Column(nullable = false)
    private LocalDate holidayDate;

    @Column(nullable = false)
    private Boolean isOpen;

    @Column
    private LocalTime openTime;

    @Column
    private LocalTime closeTime;

    @Column
    private String note;
} // 공휴일이 정보가 담긴 json 파일로 공휴일에는 예약을 막을 거임.