package kr.co.ync.projectA.domain.restaurantDetail.entity;

import jakarta.persistence.*;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "tbl_restaurant_detail",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_restaurant_detail_restaurant_id",
                        columnNames = "restaurant_id"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString(exclude = "restaurant")
public class RestaurantDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false, unique = true)
    private RestaurantEntity restaurant;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean privateRoom;     // 個室

    @Column(nullable = false)
    private Boolean smoking;         // 喫煙可否

    @Column(nullable = false)
    private Boolean unlimitDrink;    // 飲み放題

    @Column(nullable = false)
    private Boolean unlimitFood;     // 食べ放題

    @Column(nullable = false)
    private Boolean parkingArea;     // 駐車場

    /* =========================
       결제 수단 (복수 선택)
       ========================= */

    /**
     * ✅ 결제수단을 여러 개 관리 (CASH, CREDIT_CARD, E_MONEY, QR_PAY ...)
     * - 별도 테이블: tbl_restaurant_payment_method
     * - joinColumn: restaurant_detail_id
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "tbl_restaurant_payment_method",
            joinColumns = @JoinColumn(name = "restaurant_detail_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    @Builder.Default
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    private Integer seatCount;

    @Column(length = 50)
    private String averagePrice;

    public void update(
            String description,
            Boolean privateRoom,
            Boolean smoking,
            Boolean unlimitDrink,
            Boolean unlimitFood,
            Boolean parkingArea,
            Integer seatCount,
            String averagePrice,
            Set<PaymentMethod> paymentMethods
    ) {
        this.description = description;

        this.privateRoom = Boolean.TRUE.equals(privateRoom);
        this.smoking = Boolean.TRUE.equals(smoking);
        this.unlimitDrink = Boolean.TRUE.equals(unlimitDrink);
        this.unlimitFood = Boolean.TRUE.equals(unlimitFood);
        this.parkingArea = Boolean.TRUE.equals(parkingArea);

        this.seatCount = seatCount;
        this.averagePrice = averagePrice;

        // ✅ 결제수단 Set 갱신 (Orphan 개념: ElementCollection은 clear 후 addAll이 안전)
        this.paymentMethods.clear();
        if (paymentMethods != null) {
            this.paymentMethods.addAll(paymentMethods);
        }
    }

    public static RestaurantDetailEntity createDefault(RestaurantEntity restaurant) {
        return RestaurantDetailEntity.builder()
                .restaurant(restaurant)
                .description(null)
                .privateRoom(false)
                .smoking(false)
                .unlimitDrink(false)
                .unlimitFood(false)
                .parkingArea(false)
                .seatCount(null)
                .averagePrice(null)
                .paymentMethods(new HashSet<>())
                .build();
    }
}
