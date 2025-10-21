package kr.co.ync.projectA.domain.restaurant.repository;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    /* ================= 일반 사용자용 ================= */
    int countByOwner(MemberEntity owner);
    
    // 레스토랑 페이징 기능
    Page<RestaurantEntity> findByOwnerId(Long ownerId, Pageable pageable);
    
    // 지역별 조회
    List<RestaurantEntity> findByArea(String area);

    // 이름 검색 (사용자용: 리스트 반환)
    List<RestaurantEntity> findByNameContaining(String keyword);

    // 카테고리별 조회 (카테고리 조인)
    @Query(
            value = """
            select distinct r
            from RestaurantEntity r
            join r.categoryMappings m
            join m.category c
            where c.name = :categoryName
        """,
            countQuery = """
            select count(distinct r)
            from RestaurantEntity r
            join r.categoryMappings m
            join m.category c
            where c.name = :categoryName
        """
    )
    Page<RestaurantEntity> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);


    /* ================= 관리자 페이지용 ================= */

    // 🔍 페이징 기반 이름 검색
    Page<RestaurantEntity> findByNameContaining(String name, Pageable pageable);

    // 🔍 주소 검색
    Page<RestaurantEntity> findByAddressContaining(String address, Pageable pageable);

    // 🔍 ID 정확히 검색 (페이징 구조 유지)
    @Query("SELECT r FROM RestaurantEntity r WHERE r.id = :id")
    Page<RestaurantEntity> findByIdExact(@Param("id") Long id, Pageable pageable);

    List<RestaurantEntity> findByOwnerId(Long ownerId);

}
