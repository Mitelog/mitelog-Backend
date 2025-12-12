package kr.co.ync.projectA.domain.restaurantDetail.dto.response;

import kr.co.ync.projectA.domain.restaurantDetail.entity.PaymentMethod;

import java.util.Set;

public record RestaurantDetailResponse(
        String description,
        Boolean privateRoom,
        Boolean smoking,
        Boolean unlimitDrink,
        Boolean unlimitFood,
        Boolean parkingArea,
        Integer seatCount,
        String averagePrice,
        Set<PaymentMethod> paymentMethods
) {}
