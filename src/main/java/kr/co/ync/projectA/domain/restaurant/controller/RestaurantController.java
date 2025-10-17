package kr.co.ync.projectA.domain.restaurant.controller;

import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.service.RestaurantService;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /** 등록 */
    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<RestaurantResponse> register(@RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.register(request));
    }

    /** 전체 조회 */
    @GetMapping
    public ResponseEntity<Page<RestaurantResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(restaurantService.getAll(PageRequest.of(page, size)));
    }

    /** 상세 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    /** 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long id,
            @RequestBody RestaurantRequest request
    ) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** 지역별 조회 */
    @GetMapping("/area/{area}")
    public ResponseEntity<List<RestaurantResponse>> getByArea(@PathVariable String area) {
        return ResponseEntity.ok(restaurantService.getByArea(area));
    }

    /** 이름 검색 */
    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponse>> searchByName(@RequestParam String keyword) {
        return ResponseEntity.ok(restaurantService.searchByName(keyword));
    }

    /** 카테고리별 조회 */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<RestaurantResponse>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(restaurantService.getByCategory(category, PageRequest.of(page, size)));
    }

    /** 내 가게 조회 (로그인 사용자 기준) */
    @GetMapping("/my-restaurants")
    public ResponseEntity<ResponseDTO<?>> getMyRestaurants(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<RestaurantResponse> restaurants = restaurantService.findByOwnerId(user.getId(), page, size);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .status(200)
                        .msg("내 가게 목록 조회 성공")
                        .data(restaurants)
                        .build()
        );
    }
}
