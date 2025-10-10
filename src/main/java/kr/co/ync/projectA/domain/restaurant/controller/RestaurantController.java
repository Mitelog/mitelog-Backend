package kr.co.ync.projectA.domain.restaurant.controller;

import kr.co.ync.projectA.domain.restaurant.dto.Restaurant;
import kr.co.ync.projectA.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * 식당 등록
     */
    @PostMapping
    public ResponseEntity<Restaurant> register(@RequestBody Restaurant restaurant) {
        Restaurant saved = restaurantService.register(restaurant);
        return ResponseEntity.ok(saved);
    }

    /**
     * 식당 전체 조회
     */
    @GetMapping
    public ResponseEntity<Page<Restaurant>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(restaurantService.getAll(PageRequest.of(page, size)));
    }

    /**
     * 식당 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    /**
     * 식당 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 지역별 조회
     */
    @GetMapping("/area/{area}")
    public ResponseEntity<List<Restaurant>> getByArea(@PathVariable String area) {
        return ResponseEntity.ok(restaurantService.getByArea(area));
    }

    /**
     * 카테고리별 조회 (페이징)
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Restaurant>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                restaurantService.getByCategory(category, PageRequest.of(page, size))
        );
    }

    /**
     * 식당 이름 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchByName(@RequestParam String keyword) {
        return ResponseEntity.ok(restaurantService.searchByName(keyword));
    }
}
