package com.spring.client.payment.service;

import com.spring.client.payment.domain.Payment;
import com.spring.payment.vo.PaymentRequest;

public interface PaymentService {

	void paymentProcess(PaymentRequest paymentRequest);

	void paymentCancel(PaymentRequest paymentRequest);

	
	public String getBypaymentId(String paymentId);

	Payment getBypaymentNo(Long paymentNo);

	Payment getByresNo(Long rno);

	

}
