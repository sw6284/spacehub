package com.spring.client.review.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "spacehub_reply")
@SequenceGenerator(name = "spacehub_reply_generator", sequenceName = "spacehub_reply_seq", initialValue = 1, allocationSize = 1)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_reply_generator")
    @Column(name = "reply_no")
    private Long replyNo;

    @ManyToOne
    @JoinColumn(name = "rev_no", nullable = false)
    private Review review;

    @Column(name = "adm_no", nullable = false)
    private int admNo;

    @Column(name = "reply_content", length = 200, nullable = false)
    private String replyContent;

    @CreationTimestamp
    @Column(name = "reply_date", nullable = false)
    @ColumnDefault(value = "sysdate")
    private LocalDateTime replyDate;
}
