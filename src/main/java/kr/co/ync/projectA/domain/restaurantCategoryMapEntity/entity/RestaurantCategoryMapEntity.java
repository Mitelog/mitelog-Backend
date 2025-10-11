package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.category.entity.CategoryEntity;

@Entity
@Table(name = "tbl_restaurant_category_map")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"restaurant", "category"})
public class RestaurantCategoryMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ✅ 식당 FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    /** ✅ 카테고리 FK */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}
