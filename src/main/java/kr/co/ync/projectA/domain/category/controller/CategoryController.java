package kr.co.ync.projectA.domain.category.controller;

import kr.co.ync.projectA.domain.category.entity.CategoryEntity;
import kr.co.ync.projectA.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ✅ CategoryController
 * 프론트엔드에서 카테고리 목록을 조회하기 위한 컨트롤러
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    /** ✅ 전체 카테고리 목록 조회 */
    @GetMapping
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}
