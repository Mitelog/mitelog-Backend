package kr.co.ync.projectA.domain.restaurant.controller;

import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /** ✅ 가게 등록 */
    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<RestaurantResponse> register(@RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.register(request));
    }

    /** ✅ 통합 조회 (검색 + 필터 + 전체 조회)
     * 프론트에서 아래 형태로 요청:
     *   /api/restaurants?keyword=ラーメン&region=東京都&category=和食
     */
    @GetMapping
    public ResponseEntity<Page<RestaurantResponse>> getRestaurants(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                restaurantService.searchRestaurants(keyword, region, category, PageRequest.of(page, size))
        );
    }

    /** ✅ 상세 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    /** ✅ 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long id,
            @RequestBody RestaurantRequest request
    ) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    /** ✅ 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
