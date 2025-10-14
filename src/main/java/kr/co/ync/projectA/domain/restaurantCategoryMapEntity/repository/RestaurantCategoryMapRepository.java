package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.repository;

import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * ✅ 식당-카테고리 매핑 테이블 Repository
 */
@Repository
public interface RestaurantCategoryMapRepository extends JpaRepository<RestaurantCategoryMapEntity, Long> {

    /** 특정 식당의 모든 매핑 삭제 */
    void deleteByRestaurantId(Long restaurantId);

    /** 특정 식당의 매핑 전체 조회 (필요 시 사용) */
    List<RestaurantCategoryMapEntity> findByRestaurantId(Long restaurantId);
}
