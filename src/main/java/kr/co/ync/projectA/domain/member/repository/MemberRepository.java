package kr.co.ync.projectA.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;

import java.util.Optional;

<<<<<<< HEAD
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
    boolean existsByEmail(String email);
=======
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    // Query Method
    Optional<MemberEntity> findByEmail(String email);
>>>>>>> 32b1cf3 (Inital commit: Spring Boot project setup)
}
