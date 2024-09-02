package com.spring.client.reservation.service;



import java.time.Instant;
import java.util.Optional;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.spring.client.reservation.domain.Reservation;
import com.spring.client.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRespository;
	private final TaskScheduler taskScheduler;
	
	@Override
	public Reservation resDetail(Long resNo) {
		Optional<Reservation> resOptional = reservationRespository.findById(resNo);
		Reservation res = resOptional.get();
		return res;
	}
	
	@Override
	public Reservation reservationInsert(Reservation reservation) {
	    // 예약을 먼저 저장
	    Reservation savedReservation = reservationRespository.save(reservation);
	    
	    // 저장된 예약의 ID를 사용하여 스케줄러 실행
	    scheduleDeletion(savedReservation.getResNo(), 600000);
	    
	    return savedReservation;
	}


	private void scheduleDeletion(Long resNo, long delayInMillis) {
		Instant scheduleTime = Instant.now().plusMillis(delayInMillis);
		
		taskScheduler.schedule(new Runnable() {
			
			@Override
			public void run() {
				reservationRespository.deleteById(resNo);
				log.info(resNo+" 데이터 삭제");
			}
		}, scheduleTime);
	}


	
}
