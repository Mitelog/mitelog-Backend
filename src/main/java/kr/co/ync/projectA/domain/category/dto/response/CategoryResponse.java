package kr.co.ync.projectA.domain.category.dto.response;

import kr.co.ync.projectA.domain.category.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;

    public static CategoryResponse from(CategoryEntity e) {
        return new CategoryResponse(e.getId(), e.getName());
    }
}

