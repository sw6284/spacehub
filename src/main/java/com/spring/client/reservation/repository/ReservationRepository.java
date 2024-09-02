package com.spring.client.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.client.domain.Member;
import com.spring.client.reservation.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    Reservation findBymemberNo(Member memberNo);
    Reservation findByresRequest(String resRequest);
    Reservation findByresState(String resState);
    Reservation findByresNo(Long resNo);
    
    List<Reservation> findByresNameContaining(String resName);
    List<Reservation> findByresDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Reservation> findByresCreateAtBetween(LocalDateTime startDate, LocalDateTime endDate); 

    @Query("SELECT res FROM Reservation res ORDER BY res.resCreateAt DESC")
    List<Reservation> reservationList();

    @Modifying
    @Query("UPDATE Reservation res  SET res.resState = ?2 WHERE res.resNo = ?1")
    public int boardUpdate(Long resNo, String resState);
    
}
