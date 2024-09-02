package com.spring.client.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.client.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping(value="/payment")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	
	@GetMapping("/complete")
	public String completeForm() {
		
		return "client/payment/complete";
	}
	
	@PostMapping("/completeCheck")
	public String completeCheck() {
		
		return "";
	}
	
	
}
