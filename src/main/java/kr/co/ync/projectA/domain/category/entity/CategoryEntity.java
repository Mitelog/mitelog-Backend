package kr.co.ync.projectA.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class CategoryEntity { //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean available;
}
