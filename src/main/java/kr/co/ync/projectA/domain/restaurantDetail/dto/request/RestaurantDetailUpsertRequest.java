package kr.co.ync.projectA.domain.restaurantDetail.dto.request;

import kr.co.ync.projectA.domain.restaurantDetail.entity.PaymentMethod;

import java.util.Set;

public record RestaurantDetailUpsertRequest(

        // 소개문
        String description,

        // 시설 / 정책
        Boolean privateRoom,
        Boolean smoking,
        Boolean unlimitDrink,
        Boolean unlimitFood,
        Boolean parkingArea,

        // 추가 정보
        Integer seatCount,
        String averagePrice,

        // 결제 수단 (복수 선택)
        Set<PaymentMethod> paymentMethods

) {}
