package com.spring.payment.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
	
	
	private String paymentId;
	private String tid;
	private String orderName;
	private int totalAmount;
	private String payMethod;
	private Long res_no;
}
