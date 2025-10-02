package kr.co.ync.projectA.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;

@Entity
@Table(name = "tbl_restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
<<<<<<< HEAD
public class RestaurantEntity extends BaseTimeEntity {

=======
@EnableJpaAuditing
public class RestaurantEntity extends BaseTimeEntity {
>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity owner;
=======
    @JoinColumn(name = "email", nullable = false)//fk
    private MemberEntity email;
>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String address;

<<<<<<< HEAD
    @Column(length = 255, nullable = false)

    private String area;

    @Column(length = 255, nullable = false)
    private String phone;

    @Column
    private String image;

=======
    @Column(length = 255, nullable = false) //area 테이블 따로 만들게 되면 타입을 int로 변경
    private String area;

    @Column(length = 255, nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private Boolean reserAvail;

    @Column
    private String image;

    //이거 꼭 물어보기 OneToMany 왜 쓰는지
//    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RestaurantCategoryMapEntity> categoryMappings = new ArrayList<>();

>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)
}
