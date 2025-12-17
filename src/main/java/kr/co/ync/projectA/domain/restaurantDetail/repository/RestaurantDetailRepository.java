package kr.co.ync.projectA.domain.restaurantDetail.repository;

import kr.co.ync.projectA.domain.restaurantDetail.entity.RestaurantDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantDetailRepository extends JpaRepository<RestaurantDetailEntity, Long> {

    /**
     * ✅ restaurant_id(=RestaurantEntity PK)로 RestaurantDetail 조회
     * - RestaurantDetailEntity는 restaurant_id에 unique가 걸려 있으므로 최대 1건
     */
    Optional<RestaurantDetailEntity> findByRestaurant_Id(Long restaurantId);

    /**
     * ✅ 등록/수정(upsert) 전에 존재 여부만 빠르게 체크하고 싶을 때 사용 가능
     */
    boolean existsByRestaurant_Id(Long restaurantId);

    /**
     * ✅ 식당 삭제 시 detail도 같이 정리해야 하는 케이스가 있으면 사용
     * (DB FK on delete cascade를 안 쓰는 경우)
     */
    void deleteByRestaurantId(Long restaurantId);

    boolean existsByRestaurantId(Long restaurantId);
}
