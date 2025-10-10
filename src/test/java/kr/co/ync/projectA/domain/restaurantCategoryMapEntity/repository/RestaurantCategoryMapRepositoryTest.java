package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.co.ync.projectA.domain.category.entity.CategoryEntity;
import kr.co.ync.projectA.domain.category.repository.CategoryRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
class RestaurantCategoryMapRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RestaurantCategoryMapRepository restaurantCategoryMapRepository;

//    @Test
//    void testInsertRestaurantCategoryMap() {
//        List<CategoryEntity> categories = categoryRepository.findAll();
//
//        IntStream.rangeClosed(1, 30)
//                .forEach(restaurantId -> {
//                    RestaurantEntity restaurantEntity =
//                            restaurantRepository.findById((long) restaurantId)
//                                    .orElseThrow();
//
//                    categories.stream()
//                            .limit(5) // 예: 상위 5개만
//                            .forEach(category -> {
//                                RestaurantCategoryMapEntity map = RestaurantCategoryMapEntity.builder()
//                                        .restaurantId(restaurantEntity)
//                                        .category(category)
//                                        .build();
//
//                                restaurantCategoryMapRepository.save(map);
//                            });
//
//                });
//    }
}