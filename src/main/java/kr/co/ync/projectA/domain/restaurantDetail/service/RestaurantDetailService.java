package kr.co.ync.projectA.domain.restaurantDetail.service;

import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.restaurantDetail.dto.request.RestaurantDetailUpsertRequest;
import kr.co.ync.projectA.domain.restaurantDetail.dto.response.RestaurantDetailResponse;
import kr.co.ync.projectA.domain.restaurantDetail.entity.RestaurantDetailEntity;
import kr.co.ync.projectA.domain.restaurantDetail.repository.RestaurantDetailRepository;
import kr.co.ync.projectA.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantDetailService {

    private final RestaurantDetailRepository restaurantDetailRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantDetailResponse getDetail(Long restaurantId) {
        RestaurantDetailEntity detail = restaurantDetailRepository.findByRestaurant_Id(restaurantId)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "RestaurantDetail not found."
                ));

        return toResponse(detail);
    }

    public RestaurantDetailResponse getDetailOrNull(Long restaurantId) {
        return restaurantDetailRepository.findByRestaurant_Id(restaurantId)
                .map(this::toResponse)
                .orElse(null);
    }

    @Transactional
    public RestaurantDetailResponse create(Long restaurantId, Long requesterId, RestaurantDetailUpsertRequest req) {
        if (req == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Request body is null.");
        }

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Restaurant not found."
                ));

        validateOwner(restaurant, requesterId);

        if (restaurantDetailRepository.existsByRestaurant_Id(restaurantId)) {
            throw new CustomException(HttpStatus.CONFLICT.value(), "RestaurantDetail already exists.");
        }

        RestaurantDetailEntity detail = RestaurantDetailEntity.createDefault(restaurant);

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

        return toResponse(restaurantDetailRepository.save(detail));
    }

    @Transactional
    public RestaurantDetailResponse upsert(Long restaurantId, Long requesterId, RestaurantDetailUpsertRequest req) {
        if (req == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Request body is null.");
        }

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.NOT_FOUND.value(),
                        "Restaurant not found."
                ));

        validateOwner(restaurant, requesterId);

        RestaurantDetailEntity detail = restaurantDetailRepository.findByRestaurant_Id(restaurantId)
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

        return toResponse(restaurantDetailRepository.save(detail));
    }

    private void validateOwner(RestaurantEntity restaurant, Long requesterId) {
        if (requesterId == null) {
            throw new CustomException(HttpStatus.FORBIDDEN.value(), "Authentication required.");
        }

        Long ownerId = restaurant.getOwner() != null ? restaurant.getOwner().getId() : null;

        if (ownerId == null || !ownerId.equals(requesterId)) {
            throw new CustomException(HttpStatus.FORBIDDEN.value(), "Not allowed.");
        }
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

    @Transactional
    public void upsertDetail(RestaurantEntity restaurant, RestaurantDetailUpsertRequest req) {
        if (req == null) return;

        RestaurantDetailEntity detail = restaurantDetailRepository.findByRestaurant_Id(restaurant.getId())
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

}
