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

    @PostMapping
    public ResponseEntity<Restaurant> register(@RequestBody Restaurant restaurant) {
        Restaurant saved = restaurantService.register(restaurant);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll() {
        return ResponseEntity.ok(restaurantService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

//    @GetMapping("/area/{area}")
//    public ResponseEntity<List<Restaurant>> getByArea(@PathVariable String area) {
//        return ResponseEntity.ok(restaurantService.getByArea(area));
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<Page<Restaurant>> getByCategory(
//            @PathVariable String category,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        return ResponseEntity.ok(
//                restaurantService.getByCategory(category, PageRequest.of(page, size))
//        );
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Restaurant>> searchByName(@RequestParam String keyword) {
//        return ResponseEntity.ok(restaurantService.searchByName(keyword));
//    }
}
