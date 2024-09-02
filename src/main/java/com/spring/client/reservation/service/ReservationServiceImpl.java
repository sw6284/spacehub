package com.spring.client.reservation.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

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

    private final ReservationRepository reservationRepository;
    private final TaskScheduler taskScheduler;  

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Override
    public Reservation resDetail(Long resNo) {
        Optional<Reservation> resOptional = reservationRepository.findById(resNo);
        return resOptional.orElse(null);
    }

    @Override
    public Reservation reservationInsert(Reservation reservation) {
        // 예약을 먼저 저장
        Reservation savedReservation = reservationRepository.save(reservation);

        // 저장된 예약의 ID를 사용하여 스케줄러 실행
        scheduleDeletion(savedReservation.getResNo(), 600000);

        return savedReservation;
    }

    private void scheduleDeletion(Long resNo, long delayInMillis) {
        Instant scheduleTime = Instant.now().plusMillis(delayInMillis);

        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            reservationRepository.deleteById(resNo);
            log.info(resNo + " 데이터 삭제");
        }, scheduleTime);

        // 스케줄링된 작업을 맵에 저장하여 추적할 수 있게 함
        scheduledTasks.put(resNo, scheduledTask);
    }

    // 예약 삭제 스케줄 취소 메서드
    public void cancelScheduledDeletion(Long resNo) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(resNo);
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(true);
            log.info("Scheduled deletion for reservation {} has been canceled", resNo);
            scheduledTasks.remove(resNo);
        }
    }
}

