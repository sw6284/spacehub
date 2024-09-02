package com.spring.client.reservation.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.spring.admin.space.domain.Space;
import com.spring.client.domain.Member;

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
@Table(name = "spacehub_reservation")
@SequenceGenerator(name = "spacehub_reservation_generator", sequenceName = "spacehub_reservation_seq", initialValue = 1, allocationSize = 1)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_reservation_generator")
    @Column(name = "r_no")
    private Long resNo;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member memberNo;

    @ManyToOne
    @JoinColumn(name = "sp_no", nullable = false)
    private Space spNo;

    @Column(name = "r_name", length = 20, nullable = false)
    private String resName;

    @Column(name = "r_date", nullable = false)
    private LocalDateTime resDate;

    @Column(name = "r_start_time", nullable = false)
    private LocalDateTime resStartTime;

    @Column(name = "r_use_time", nullable = false)
    private int resUseTime;

    @Column(name = "r_personnel", nullable = false)
    private int resPersonnel;

    @Column(name = "r_request")
    private String resRequest;

    @Column(name = "r_amount", nullable = false)
    private int resAmount;

    @Column(name = "r_state", nullable = false)
    private String resState;

    @CreationTimestamp
    @Column(name = "r_create_at", nullable = false)
    private LocalDateTime resCreateAt;

    @UpdateTimestamp
    @Column(name = "r_update_at")
    private LocalDateTime resUpdateAt;
    
  
}
