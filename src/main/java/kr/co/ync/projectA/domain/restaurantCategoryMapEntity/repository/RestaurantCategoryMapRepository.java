package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantCategoryMapRepository extends JpaRepository<RestaurantCategoryMapEntity, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RestaurantCategoryMapEntity m where m.restaurant.id = :restaurantId")
    void deleteByRestaurant_Id(@Param("restaurantId")Long restaurantId);
    boolean existsByRestaurant_IdAndCategory_Id(Long restaurantId, Long categoryId);
}
