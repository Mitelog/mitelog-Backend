package kr.co.ync.projectA.domain.restaurant.controller;

import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantSearchRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.service.RestaurantService;
import kr.co.ync.projectA.domain.restaurantDetail.dto.request.RestaurantDetailUpsertRequest;
import kr.co.ync.projectA.domain.restaurantDetail.dto.response.RestaurantDetailResponse;
import kr.co.ync.projectA.domain.restaurantDetail.service.RestaurantDetailService;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantDetailService restaurantDetailService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<RestaurantResponse> register(@RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.register(request));
    }

    @GetMapping
    public ResponseEntity<Page<RestaurantResponse>> getAll(
            @ModelAttribute RestaurantSearchRequest cond,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(restaurantService.getAll(cond, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long id,
            @RequestBody RestaurantRequest request
    ) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/area/{area}")
    public ResponseEntity<List<RestaurantResponse>> getByArea(@PathVariable String area) {
        return ResponseEntity.ok(restaurantService.getByArea(area));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponse>> searchByName(@RequestParam String keyword) {
        return ResponseEntity.ok(restaurantService.searchByName(keyword));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<RestaurantResponse>> getByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(restaurantService.getByCategory(category, PageRequest.of(page, size)));
    }

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

    @GetMapping("/popular")
    public ResponseEntity<List<RestaurantResponse>> getPopularRestaurants(
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(restaurantService.getPopularRestaurants(size));
    }

    @GetMapping("/new")
    public ResponseEntity<List<RestaurantResponse>> getNewRestaurants(
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(restaurantService.getNewRestaurants(size));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<RestaurantDetailResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantDetailService.getDetail(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/detail")
    public ResponseEntity<RestaurantDetailResponse> createDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody RestaurantDetailUpsertRequest request
    ) {
        return ResponseEntity.ok(restaurantDetailService.create(id, user.getId(), request));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/detail")
    public ResponseEntity<RestaurantDetailResponse> upsertDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody RestaurantDetailUpsertRequest request
    ) {
        return ResponseEntity.ok(restaurantDetailService.upsert(id, user.getId(), request));
    }
}
