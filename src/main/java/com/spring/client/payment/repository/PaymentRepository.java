package com.spring.client.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.client.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

	Payment findBypayNo(Long paymentNo);
	Payment findBypayPaymentId(String paymentId);
	
	@Query("SELECT pay.payNo FROM Payment pay WHERE pay.payPaymentId = ?1")
    String paymentNoBypaymentId(String paymentId);


	Payment findByReservationResNo(Long resNo);
}
