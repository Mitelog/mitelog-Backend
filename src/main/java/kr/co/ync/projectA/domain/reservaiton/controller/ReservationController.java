package kr.co.ync.projectA.domain.reservaiton.controller;

import kr.co.ync.projectA.domain.reservaiton.dto.request.ReservationRequest;
import kr.co.ync.projectA.domain.reservaiton.dto.response.ReservationResponse;
import kr.co.ync.projectA.domain.reservaiton.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 등록
     */
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest.Create request
    ) {
        ReservationResponse response = reservationService.createReservation(request);

        // Location 헤더에 생성된 리소스 경로 넣어주는 패턴
        return ResponseEntity
                .created(URI.create("/api/reservations/" + response.id()))
                .body(response);
    }

    /**
     * 예약 삭제 (취소)
     */
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long reservationId
    ) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 가게 기준 예약 조회
     * 예: /api/reservations/restaurant/3
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByRestaurant(
            @PathVariable Long restaurantId
    ) {
        List<ReservationResponse> result = reservationService.getReservationsByRestaurant(restaurantId);
        return ResponseEntity.ok(result);
    }

    /**
     * 고객 기준 예약 조회
     * 예: /api/reservations/member/5
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByMember(
            @PathVariable Long memberId
    ) {
        List<ReservationResponse> result = reservationService.getReservationsByMember(memberId);
        return ResponseEntity.ok(result);
    }

    /**
     * (선택) 단건 예약 조회
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(
            @PathVariable Long reservationId
    ) {
        ReservationResponse response = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(response);
    }
}
