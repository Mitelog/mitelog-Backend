package kr.co.ync.projectA.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.menu.entity.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
