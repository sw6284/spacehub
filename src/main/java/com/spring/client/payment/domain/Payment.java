package com.spring.client.payment.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.spring.client.reservation.domain.Reservation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "spacehub_payment")
@SequenceGenerator(name = "spacehub_payment_generator", sequenceName = "spacehub_payment_seq", initialValue = 1, allocationSize = 1)
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_payment_generator")
	@Column(name = "p_no")
	private Long payNo;

	@Column(name = "member_no", nullable = false)
	private Long memberNo;
	

	@ManyToOne
	@JoinColumn(name = "r_no", nullable = false)
	private Reservation reservation;

	@Column(name = "pay_name", nullable = true)
	private String payName;
	
	@Column(name = "p_amount", nullable = false)
	private int payAmount;

	@Column(name = "p_method", length = 50, nullable = false)
	private String payMethod;

	@Column(name = "p_state", length = 50, nullable = false)
	private String payState;

	@CreationTimestamp
	@Column(name = "p_created_at", nullable = false)
	private LocalDateTime payCreatedAt;

	@UpdateTimestamp
	@Column(name = "p_update_at")
	private LocalDateTime payUpdateAt;

	@Column(name = "p_tid", nullable = false)
	private String payTid;

	@Column(name = "p_paymentId", nullable = false)
	private String payPaymentId;

	@Column(name = "p_cancelReason", nullable = true)
	private String payCancelReason;
	
	
}
