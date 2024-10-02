package com.spring.client.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.client.auth.service.UserMypageService;
import com.spring.client.domain.Member;
import com.spring.client.payment.domain.Payment;
import com.spring.client.payment.service.PaymentService;
import com.spring.payment.vo.PaymentRequest;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	private final UserMypageService userMypageService;

	@GetMapping("/complete/{paymentNo}")
	public String completeForm(@PathVariable("paymentNo") Long paymentNo, HttpSession session, Model model) {

		String memberId = (String) session.getAttribute("loggedInUser");

		// 회원 정보 불러오기
		Member loggedInUser = userMypageService.getMemberById(memberId);

		// 결제 정보 불러오기
		Payment payment = paymentService.getBypaymentNo(paymentNo);

		if (payment.getMemberNo() != loggedInUser.getMemberNo()) {
			return "error/403";
		}

		model.addAttribute("payment", payment);
		return "client/payment/complete";
	}

	@PostMapping("/paymentCheck")
	public ResponseEntity<String> completeCheck(@RequestBody PaymentRequest paymentRequest) {
		try {
			paymentService.paymentProcess(paymentRequest);
			String paymentNo = paymentService.getBypaymentId(paymentRequest.getPaymentId());
			return ResponseEntity.ok(paymentNo); // 결제 성공 시 클라이언트에게 "success" 메시지 반환
		} catch (IllegalArgumentException e) {
			// 결제 유효성 검사 실패 등으로 인한 오류를 클라이언트에게 전달
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			// 기타 오류 처리
			log.info("------------------------------error" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}

	@PostMapping("/payCancel")
	public ResponseEntity<String> paymentCancel(@RequestBody PaymentRequest paymentRequest) {
		
		if(paymentRequest.getReason() == null) {
			paymentRequest.setReason("단순변심");
		}
		
		if(paymentRequest.getPaymentId() == null) {
			Long resNo = paymentRequest.getResNo();
			Payment payment = paymentService.getByresNo(resNo);
			paymentRequest.setReason("업체측 사정에의한 취소");
			paymentRequest.setPaymentId(payment.getPayPaymentId());
		}
		
		try {
			paymentService.paymentCancel(paymentRequest);
			

			return ResponseEntity.ok("success");
		} catch (IllegalArgumentException e) {
			// 결제 유효성 검사 실패 등으로 인한 오류를 클라이언트에게 전달
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			// 기타 오류 처리
			log.info("------------------------------error" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
	}

	@GetMapping("/cancelPage")
	public String cancelPage(@RequestParam("rno") Long rno,@RequestParam("rst") String rst, @RequestParam("rnm") String rnm, @RequestParam("ret") String ret, Model model) {

		Payment payment = paymentService.getByresNo(rno);

		// 모델에 데이터 추가
		model.addAttribute("payment", payment);
		model.addAttribute("startTime", rst);
		model.addAttribute("endTime", ret); 
		model.addAttribute("resName", rnm); 

		// 팝업창에 표시할 뷰 반환
		return "client/payment/cancelPage"; 
	}

}
