package kr.co.ync.projectA.domain.restaurant.entity;

import jakarta.persistence.*;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;
import lombok.*;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"owner", "categoryMappings"})
public class RestaurantEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity owner;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 255, nullable = false)
    private String area;

    @Column(length = 255, nullable = false)
    private String phone;

    @Column
    private String image;

    @Builder.Default
    @Column(nullable = false)
    private Double averageRating = 0.0;

    // ✅ 식당 ↔ 카테고리 매핑
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantCategoryMapEntity> categoryMappings = new ArrayList<>();

    public void update(String name, String address, String area, String phone, String image) {
        this.name = name;
        this.address = address;
        this.area = area;
        this.phone = phone;
        this.image = image;
    }
}
