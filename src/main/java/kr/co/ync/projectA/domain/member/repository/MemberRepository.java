package kr.co.ync.projectA.domain.member.repository;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // ✅ 기존 로그인 관련 메서드 (유지)
    Optional<MemberEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    // ✅ 관리자 페이지용 페이징 & 검색
    Page<MemberEntity> findByNameContaining(String name, Pageable pageable);
    Page<MemberEntity> findByEmailContaining(String email, Pageable pageable);
    Page<MemberEntity> findByPhoneContaining(String phone, Pageable pageable);

    // ✅ id 정확히 검색 (페이징 구조 유지)
    @Query("SELECT m FROM MemberEntity m WHERE m.id = :id")
    Page<MemberEntity> findByIdExact(@Param("id") Long id, Pageable pageable);
}
