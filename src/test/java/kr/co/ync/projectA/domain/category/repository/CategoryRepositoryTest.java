package kr.co.ync.projectA.domain.category.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.co.ync.projectA.domain.category.entity.CategoryEntity;

import java.util.stream.IntStream;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testInsertCategory() {
        IntStream.rangeClosed(1, 30)
                .forEach(i -> {
                    CategoryEntity categoryEntity = CategoryEntity
                            .builder()
                            .name("재환식" + i)
                            .available(true)
                            .build();

                    categoryRepository.save(categoryEntity);
                });
    }
}