package kr.co.ync.projectA.domain.follow.entity;

import jakarta.persistence.*;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_follow",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "following_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 팔로우를 거는 사람 (로그인 유저)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private MemberEntity follower;

    // 팔로우를 받는 사람 (대상 유저)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private MemberEntity following;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
