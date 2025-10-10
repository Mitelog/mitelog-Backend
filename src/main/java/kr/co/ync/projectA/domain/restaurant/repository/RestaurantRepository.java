package kr.co.ync.projectA.domain.restaurant.repository;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    List<RestaurantEntity> findByArea(String area);

    Page<RestaurantEntity> findByCategoryName(String categoryName, Pageable pageable);

    List<RestaurantEntity> findByNameContaining(String keyword);
}
