package kr.co.ync.projectA.domain.reservaiton.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.reservaiton.entity.ReservationEntity;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
}
