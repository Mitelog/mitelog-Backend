package kr.co.ync.projectA.domain.restaurant.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantSearchRequest {

    // 키워드(店名/料理名… 현재는 우선 name 기준으로 처리)
    private String keyword;

    // 지역
    private String area;

    // 카테고리(드롭다운)
    private String category;

    /* ===== こだわり条件 ===== */

    // UI: クレジットカード可
    // ✅ 우리 DB는 paymentMethods(Set<PaymentMethod>)로 관리하므로 "CREDIT_CARD 포함"으로 필터링한다.
    private Boolean creditCard;

    private Boolean parkingArea;
    private Boolean privateRoom;
    private Boolean smoking;
    private Boolean unlimitDrink;
    private Boolean unlimitFood;
}
