package kr.co.ync.projectA.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.category.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
