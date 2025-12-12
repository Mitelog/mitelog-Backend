package kr.co.ync.projectA.domain.restaurantDetail.service;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurantDetail.dto.response.RestaurantDetailResponse;
import kr.co.ync.projectA.domain.restaurantDetail.dto.request.RestaurantDetailUpsertRequest;
import kr.co.ync.projectA.domain.restaurantDetail.entity.RestaurantDetailEntity;
import kr.co.ync.projectA.domain.restaurantDetail.repository.RestaurantDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantDetailService {

    private final RestaurantDetailRepository restaurantDetailRepository;

    public RestaurantDetailResponse getDetailOrNull(Long restaurantId) {
        return restaurantDetailRepository.findByRestaurant_Id(restaurantId)
                .map(this::toResponse)
                .orElse(null);
    }

    @Transactional
    public void upsertDetail(RestaurantEntity restaurant, RestaurantDetailUpsertRequest req) {

        if (req == null) return;

        RestaurantDetailEntity detail = restaurantDetailRepository
                .findByRestaurant_Id(restaurant.getId())
                .orElseGet(() -> RestaurantDetailEntity.createDefault(restaurant));

        detail.update(
                req.description(),
                req.privateRoom(),
                req.smoking(),
                req.unlimitDrink(),
                req.unlimitFood(),
                req.parkingArea(),
                req.seatCount(),
                req.averagePrice(),
                req.paymentMethods()
        );

        restaurantDetailRepository.save(detail);
    }

    private RestaurantDetailResponse toResponse(RestaurantDetailEntity e) {
        return new RestaurantDetailResponse(
                e.getDescription(),
                e.getPrivateRoom(),
                e.getSmoking(),
                e.getUnlimitDrink(),
                e.getUnlimitFood(),
                e.getParkingArea(),
                e.getSeatCount(),
                e.getAveragePrice(),
                e.getPaymentMethods()
        );
    }
}
