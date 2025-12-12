package kr.co.ync.projectA.domain.reservaiton.dto.request;

import java.time.LocalDateTime;

public class ReservationRequest {

    /**
     * 예약 생성 요청 DTO
     * - 실제 운영에서는 memberId는 로그인 유저에서 가져가는게 보통이긴 한데
     *   지금은 단순하게 body에서 받는 형태로 둠.
     */
    public record Create(
            Long restaurantId,
            LocalDateTime visit,
            Long numPeople
    ) {}
}
