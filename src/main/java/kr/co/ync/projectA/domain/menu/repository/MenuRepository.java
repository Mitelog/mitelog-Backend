package kr.co.ync.projectA.domain.menu.repository;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.menu.entity.MenuEntity;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    List<MenuEntity> findByRestaurant(RestaurantEntity restaurant);
}
