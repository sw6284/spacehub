package com.spring.client.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.client.payment.service.PaymentService;
import com.spring.payment.vo.PaymentRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@GetMapping("/complete")
	public String completeForm() {

		return "client/payment/complete";
	}

	@PostMapping("/completeCheck")
	public ResponseEntity<String> completeCheck(@RequestBody PaymentRequest paymentRequest) {
	    try {
	        paymentService.paymentProcess(paymentRequest);
	        return ResponseEntity.ok("success"); // 결제 성공 시 클라이언트에게 "success" 메시지 반환
	    } catch (IllegalArgumentException e) {
	        // 결제 유효성 검사 실패 등으로 인한 오류를 클라이언트에게 전달
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    } catch (Exception e) {
	        // 기타 오류 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.13132323");
	    }
	}
}
