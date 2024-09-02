package com.spring.client.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.client.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{



}
