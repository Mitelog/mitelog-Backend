package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.category.entity.CategoryEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;

@Entity
@Table(name = "tbl_restaurant_category_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantCategoryMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}

