package kr.co.ync.projectA.domain.restaurantDetail.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

@Entity
@Table(name = "tbl_restaurantDetail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class RestaurantDetailEntity {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, unique = true)
    private RestaurantEntity restaurantId;

    @Column(nullable = false)
    private String openHours;

    @Column(nullable = false)
    private Boolean privateRoom;

    @Column(nullable = false)
    private Boolean smoking;

    @Column(nullable = false)
    private Boolean unlimitDrink;

    @Column(nullable = false)
    private Boolean creditCard;

    @Column(nullable = false)
    private Boolean parkingArea;

    @Column(nullable = false)
    private Boolean unlimitFood;

}
