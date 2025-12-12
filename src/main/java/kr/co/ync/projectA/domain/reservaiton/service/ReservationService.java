package kr.co.ync.projectA.domain.reservaiton.service;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.reservaiton.dto.request.ReservationRequest;
import kr.co.ync.projectA.domain.reservaiton.dto.response.ReservationResponse;
import kr.co.ync.projectA.domain.reservaiton.entity.ReservationEntity;
import kr.co.ync.projectA.domain.reservaiton.repository.ReservationRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    /**
     * 예약 생성
     */
    public ReservationResponse createReservation(Long memberId, ReservationRequest.Create request) {

        RestaurantEntity restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 식당입니다. id=" + request.restaurantId())
                );

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 회원입니다. id=" + memberId)
                );

        ReservationEntity reservation = ReservationEntity.builder()
                .restaurant(restaurant)
                .member(member)
                .visit(request.visit())
                .numPeople(request.numPeople())
                .build();

        ReservationEntity saved = reservationRepository.save(reservation);

        return toResponse(saved);
    }


    /**
     * 예약 삭제
     */
    public void deleteReservation(Long memberId, Long reservationId) {
        ReservationEntity entity = reservationRepository.findById(reservationId)
                .orElseThrow();

        if (!entity.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("본인의 예약만 삭제할 수 있습니다.");
        }

        reservationRepository.delete(entity);
    }


    /**
     * 단건 조회
     */
    @Transactional(readOnly = true)
    public ReservationResponse getReservation(Long reservationId) {
        ReservationEntity entity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다. id=" + reservationId));
        return toResponse(entity);
    }

    /**
     * 가게 기준 예약 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByRestaurant(Long restaurantId) {
        return reservationRepository.findByRestaurant_Id(restaurantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * 회원 기준 예약 목록 조회
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByMember(Long memberId) {
        return reservationRepository.findByMember_Id(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ReservationResponse toResponse(ReservationEntity entity) {
        return new ReservationResponse(
                entity.getId(),
                entity.getRestaurant().getId(),
                entity.getMember().getId(),
                entity.getVisit(),
                entity.getNumPeople()
        );
    }
}
