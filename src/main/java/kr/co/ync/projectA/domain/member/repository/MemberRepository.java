package kr.co.ync.projectA.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    // Query Method
    Optional<MemberEntity> findByEmail(String email);
}
