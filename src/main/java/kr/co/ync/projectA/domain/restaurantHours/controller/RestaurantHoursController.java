package kr.co.ync.projectA.domain.restaurantHours.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import kr.co.ync.projectA.domain.restaurantHours.dto.request.RestaurantHoursActiveRequest;
import kr.co.ync.projectA.domain.restaurantHours.dto.request.RestaurantHoursRequest;
import kr.co.ync.projectA.domain.restaurantHours.dto.response.RestaurantHoursResponse;
import kr.co.ync.projectA.domain.restaurantHours.enums.DayOfWeekType;
import kr.co.ync.projectA.domain.restaurantHours.service.RestaurantHoursService;
import lombok.RequiredArgsConstructor;

// 레스토랑 요일별 운영 시간 관련 API 컨트롤러
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/hours")
@RequiredArgsConstructor
public class RestaurantHoursController {

    private final RestaurantHoursService restaurantHoursService;

    // 요일별 운영 시간 조회
    @GetMapping
    public List<RestaurantHoursResponse> getRestaurantHours(
            @PathVariable Long restaurantId
    ) {
        return restaurantHoursService.getHours(restaurantId);
    }

    // 요일별 운영 시간 등록 (초기 세팅)
    @PostMapping
    public void initRestaurantHours(
            @PathVariable Long restaurantId,
            @RequestBody List<RestaurantHoursRequest> requests
    ) {
        restaurantHoursService.initHours(restaurantId, requests);
    }

    // 요일별 운영 시간 수정
    @PutMapping("/{dayOfWeek}")
    public RestaurantHoursResponse updateRestaurantHours(
            @PathVariable Long restaurantId,
            @PathVariable DayOfWeekType dayOfWeek,
            @RequestBody RestaurantHoursRequest request
    ) {
        return restaurantHoursService.updateHours(restaurantId, dayOfWeek, request);
    }

    // 요일별 운영 시간 활성/비활성 (휴무 설정)
    @PatchMapping("/{dayOfWeek}/active")
    public RestaurantHoursResponse updateRestaurantHoursActive(
            @PathVariable Long restaurantId,
            @PathVariable DayOfWeekType dayOfWeek,
            @RequestBody RestaurantHoursActiveRequest request
    ) {
        return restaurantHoursService.updateActive(restaurantId, dayOfWeek, request);
    }
}
