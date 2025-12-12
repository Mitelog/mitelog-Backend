package kr.co.ync.projectA.domain.reservaiton.dto.response;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long restaurantId,
        Long memberId,
        LocalDateTime visit,
        Long numPeople
) {
}
