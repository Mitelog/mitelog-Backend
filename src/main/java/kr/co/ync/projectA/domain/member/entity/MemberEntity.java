package kr.co.ync.projectA.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import kr.co.ync.projectA.domain.member.entity.enums.MemberRole;
import kr.co.ync.projectA.global.common.entity.BaseTimeEntity;

@Entity
@Table(name = "tbl_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    // 암호화된 비밀번호
    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false, unique = true)
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private MemberRole role;
}