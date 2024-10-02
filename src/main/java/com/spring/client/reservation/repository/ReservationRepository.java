package com.spring.client.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.client.reservation.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

   
    
    Reservation findByresRequest(String resRequest);
    Reservation findByresState(String resState);
    Reservation findByresNo(Long resNo);
    
    List<Reservation> findByMemberMemberNo(Long memberNo);
    List<Reservation> findByresNameContaining(String resName);
    List<Reservation> findByresDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Reservation> findByresCreateAtBetween(LocalDateTime startDate, LocalDateTime endDate); 
    List<Reservation> findByResStartTimeAfter(LocalDateTime startDate);
    List<Reservation> findAllByOrderByResStartTime();
   
    // 예약자명으로 검색
    Page<Reservation> findByResNameContaining(String resName, Pageable pageable);
    
    // 예약 날짜로 검색
    Page<Reservation> findByResStartTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    @Query("SELECT res FROM Reservation res WHERE res.member.memberNo = ?1  ORDER BY res.resStartTime DESC")
    List<Reservation> reservationList(Long memberNo);

    @Modifying
    @Query("UPDATE Reservation res  SET res.resState = ?2 WHERE res.resNo = ?1")
    public int resUpdate(Long resNo, String resState);
	
    @Modifying
    @Query("UPDATE Reservation res  SET res.resState = ?2, res.resName = ?3, res.resPhone = ?4, res.resRequest = ?5, res.resUpdateAt = ?6 WHERE res.resNo = ?1")
    public int resUpdate(Long resNo, String resState, String resName, String resPhone, String resRequest, LocalDateTime resUpdateAt);
	
	
	
}
