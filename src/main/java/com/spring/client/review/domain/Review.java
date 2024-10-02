package com.spring.client.review.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.spring.admin.space.domain.Space;
import com.spring.client.domain.Member;
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
@Table(name = "spacehub_review")
@SequenceGenerator(name = "spacehub_review_generator", sequenceName = "spacehub_review_seq", initialValue = 1, allocationSize = 1)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_review_generator")
    @Column(name = "rev_no")
    private Long revNo;

    @ManyToOne
    @JoinColumn(name = "sp_no", nullable = false)
    private Space space;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;
   
    @ManyToOne
    @JoinColumn(name = "r_no", nullable = false)
    private Reservation reservation;

    @Column(name = "rev_score", nullable = false)
    private int revScore;

    @Column(name = "rev_content", length = 200, nullable = false)
    private String revContent;

    @CreationTimestamp
    @Column(name = "rev_date", nullable = false)
    @ColumnDefault(value = "sysdate")
    private LocalDateTime revDate;
}
