package kr.co.ync.projectA.domain.menu.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

@Entity
@Table(name = "tbl_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column
    private String image;
}
