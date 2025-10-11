package kr.co.ync.projectA.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;

@Entity
@Table(name = "tbl_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString(exclude = "restaurantMappings")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ✅ 카테고리 이름 (예: 한식, 일식, 중식 등) */
    @Column(length = 255, nullable = false, unique = true)
    private String name;

    /** ✅ RestaurantCategoryMapEntity 연결 */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantCategoryMapEntity> restaurantMappings = new ArrayList<>();
}
