package com.spring.client.reservation.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.spring.client.reservation.domain.Reservation;
import com.spring.client.reservation.repository.ReservationRepository;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;
import com.spring.common.vo.SearchRequestDTO;

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

    public PageResponseDTO<Reservation> getReservationList(PageRequestDTO pageRequestDTO) {
        // Pageable 객체 생성 (0-based paging 사용을 위해 page - 1)
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        
        Page<Reservation> result = reservationRepository.findAll(pageable);  // JPA에서 기본 제공

        // PageResponseDTO를 빌드하여 반환
        return PageResponseDTO.<Reservation>withAll()
            .dtoList(result.getContent())  // 페이지 데이터 리스트
            .pageRequestDTO(pageRequestDTO)  // 요청된 페이지 정보
            .totalCount(result.getTotalElements())  // 전체 데이터 수
            .build();
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

	@Override
	public List<Reservation> getByMemberNo(Long memberNo) {
		List<Reservation> res = reservationRepository.reservationList(memberNo);
		return res;
	}
	
	 // 예약이 존재하는지 확인하는 메서드
	@Override
    public boolean isReservationExists(Long resNo) {
        // 예약 번호로 예약이 존재하는지 확인
        return reservationRepository.existsById(resNo);
    }

	@Override
	public List<Reservation> reservationList() {
		List<Reservation> res = reservationRepository.findAllByOrderByResStartTime();
		return res;
	}

	@Override
	public PageResponseDTO<Reservation> searchReservations(SearchRequestDTO searchRequestDTO, PageRequestDTO pageRequestDTO) {
	    Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
	    
	    Page<Reservation> result;
	    
	    if (searchRequestDTO.getKeyWord() != null) {
	        result = reservationRepository.findByResNameContaining(searchRequestDTO.getKeyWord(), pageable);
	    } else if (searchRequestDTO.getDateSearch() != null) {
	        result = reservationRepository.findByResStartTimeBetween(searchRequestDTO.getDateSearch(), searchRequestDTO.getDateSearch().plusDays(1), pageable);
	    } else {
	        result = reservationRepository.findAll(pageable);
	    }

	    return PageResponseDTO.<Reservation>withAll()
	        .dtoList(result.getContent())
	        .pageRequestDTO(pageRequestDTO)
	        .totalCount(result.getTotalElements())
	        .build();
	}
	
	
}
	


