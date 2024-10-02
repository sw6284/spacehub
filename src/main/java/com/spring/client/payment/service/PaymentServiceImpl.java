package com.spring.client.payment.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.spring.client.payment.domain.Payment;
import com.spring.client.payment.repository.PaymentRepository;
import com.spring.client.reservation.domain.Reservation;
import com.spring.client.reservation.repository.ReservationRepository;
import com.spring.client.reservation.service.ReservationService;
import com.spring.payment.vo.PaymentRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final ReservationRepository reservationRepository;
	private final ReservationService reservationService; // ReservationServiceImpl 주입

	@Override
	@Transactional
	public void paymentProcess(PaymentRequest paymentRequest) {

		// 결제금액 유효성 확인
		if (!validatePayment(paymentRequest)) {
			// 유효하지 않은 결제 정보일 경우 결제 취소
			cancelPayment(paymentRequest.getPaymentId(), "유효성 검사 실패로 인한 자동 취소");
			throw new IllegalArgumentException("유효하지 않은 결제 정보입니다.");
		}

		Reservation reservation = reservationRepository.findByresNo(paymentRequest.getResNo());
		Payment payment = new Payment();
		payment.setMemberNo(reservation.getMember().getMemberNo());
		payment.setPayName(paymentRequest.getResName());
		payment.setReservation(reservation);
		payment.setPayAmount(paymentRequest.getTotalAmount());
		payment.setPayMethod(paymentRequest.getPayMethod());
		payment.setPayState("결제완료");

		String tid = paymentRequest.getTid();

		if (tid == null || tid.isEmpty()) {
			tid = "TEST_TID_" + UUID.randomUUID().toString(); // 임시 tid 생성
			payment.setPayTid(tid);
		}

		payment.setPayPaymentId(paymentRequest.getPaymentId());

		// 결제 정보 저장
		paymentRepository.save(payment);

		// 예약 삭제 스케줄 취소
		reservationService.cancelScheduledDeletion(reservation.getResNo());

		reservationRepository.resUpdate(paymentRequest.getResNo(), "결제완료", paymentRequest.getResName(),
				paymentRequest.getResPhone(), paymentRequest.getResRequest(), LocalDateTime.now());

	}

	@Override
	public void paymentCancel(PaymentRequest paymentRequest) {
		String paymentId = paymentRequest.getPaymentId();
		String reason =paymentRequest.getReason();
		cancelPayment(paymentId, reason);
		
		Payment payment = paymentRepository.findBypayPaymentId(paymentId);
		Reservation reservation = payment.getReservation();
		
		payment.setPayState("결제환불");
		payment.setPayCancelReason(reason);
		
		reservation.setResState("예약취소");
		
		reservationRepository.save(reservation);
		paymentRepository.save(payment);
	}

	

	private boolean validatePayment(PaymentRequest paymentRequest) {
		Reservation reservation = reservationRepository.findByresNo(paymentRequest.getResNo());

		if (reservation == null || reservation.getResAmount() != paymentRequest.getTotalAmount()) {
			// 유효성 검사 실패
			return false;
		}

		// 유효성 검사 통과
		return true;
	}

	private void cancelPayment(String paymentId, String reason) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = "https://api.portone.io/payments/" + paymentId + "/cancel";

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");

			String requestBody = "{" + "\"reason\":\"" + reason + "\"," + "\"additionalInfo\":\"value\"" + "}";

			HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				log.info("결제 취소 성공: ", response.getBody());
			} else {
				log.error("결제 취소 실패: ", response.getStatusCode());
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			log.error("결제 취소 요청 중 오류가 발생했습니다: ", e.getMessage());
		} catch (Exception e) {
			log.error("알 수 없는 오류가 발생했습니다: ", e.getMessage());
		}
	}

	
	
	@Override
	public String getBypaymentId(String paymentId) {
		String pay = paymentRepository.paymentNoBypaymentId(paymentId);
		return pay;
	}

	@Override
	public Payment getBypaymentNo(Long paymentNo) {
		Payment payment = paymentRepository.findBypayNo(paymentNo);
		return payment;
	}

	@Override
	public Payment getByresNo(Long rno) {
		Payment payment = paymentRepository.findByReservationResNo(rno);
		return payment;
	}

}
