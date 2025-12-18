package kr.co.ync.projectA.domain.category.controller;

import kr.co.ync.projectA.domain.category.dto.response.CategoryResponse;
import kr.co.ync.projectA.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    /** ✅ 전체 카테고리 목록 조회 (DTO로만 반환) */
    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }
}
