package kr.co.ync.projectA.domain.restaurantHours.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.restaurantHours.dto.request.RestaurantHoursActiveRequest;
import kr.co.ync.projectA.domain.restaurantHours.dto.request.RestaurantHoursRequest;
import kr.co.ync.projectA.domain.restaurantHours.dto.response.RestaurantHoursResponse;
import kr.co.ync.projectA.domain.restaurantHours.entity.RestaurantHoursEntity;
import kr.co.ync.projectA.domain.restaurantHours.enums.DayOfWeekType;
import kr.co.ync.projectA.domain.restaurantHours.repository.RestaurantHoursRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantHoursService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantHoursRepository restaurantHoursRepository;

    // 요일별 운영 시간 조회
    // GET /api/restaurants/{restaurantId}/hours
    public List<RestaurantHoursResponse> getHours(Long restaurantId) {
        List<RestaurantHoursEntity> hoursList =
                restaurantHoursRepository.findByRestaurant_Id(restaurantId);

        return hoursList.stream()
                .map(RestaurantHoursResponse::fromEntity)
                .toList();
    }

    // 요일별 운영 시간 등록 (초기 세팅)
    // POST /api/restaurants/{restaurantId}/hours
    @Transactional
    public void initHours(Long restaurantId, List<RestaurantHoursRequest> requests) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다. id=" + restaurantId));

        // 이미 영업시간이 하나라도 있으면 초기 세팅 불가 (정책용)
        boolean exists = !restaurantHoursRepository.findByRestaurant_Id(restaurantId).isEmpty();
        if (exists) {
            throw new IllegalStateException("이미 영업시간이 등록된 레스토랑입니다. 초기 세팅은 한 번만 가능합니다.");
        }

        List<RestaurantHoursEntity> entities = requests.stream()
                .map(req -> RestaurantHoursEntity.builder()
                        .restaurant(restaurant)
                        .dayOfWeek(req.getDayOfWeek())
                        .openTime(req.getOpenTime())
                        .closeTime(req.getCloseTime())
                        .isOpen(req.getIsOpen())
                        .build()
                )
                .toList();

        restaurantHoursRepository.saveAll(entities);
    }

    // 요일별 운영 시간 수정
    // PUT /api/restaurants/{restaurantId}/hours/{dayOfWeek}
    @Transactional
    public RestaurantHoursResponse updateHours(
            Long restaurantId,
            DayOfWeekType dayOfWeek,
            RestaurantHoursRequest request
    ) {
        RestaurantHoursEntity entity = restaurantHoursRepository
                .findByRestaurant_IdAndDayOfWeek(restaurantId, dayOfWeek)
                .orElseThrow(() -> new IllegalArgumentException("해당 요일의 영업시간이 존재하지 않습니다. restaurantId=" + restaurantId + ", dayOfWeek=" + dayOfWeek));

        // request.getDayOfWeek()는 굳이 안 써도 됨. pathVariable 기준으로 처리.
        entity.updateHours(
                request.getOpenTime(),
                request.getCloseTime(),
                request.getIsOpen()
        );

        return RestaurantHoursResponse.fromEntity(entity);
    }

    // 요일별 운영 시간 활성/비활성 (휴무 설정)
    // PATCH /api/restaurants/{restaurantId}/hours/{dayOfWeek}/active
    @Transactional
    public RestaurantHoursResponse updateActive(
            Long restaurantId,
            DayOfWeekType dayOfWeek,
            RestaurantHoursActiveRequest request
    ) {
        RestaurantHoursEntity entity = restaurantHoursRepository
                .findByRestaurant_IdAndDayOfWeek(restaurantId, dayOfWeek)
                .orElseThrow(() -> new IllegalArgumentException("해당 요일의 영업시간이 존재하지 않습니다. restaurantId=" + restaurantId + ", dayOfWeek=" + dayOfWeek));

        entity.updateActive(request.getIsOpen());

        return RestaurantHoursResponse.fromEntity(entity);
    }
}
