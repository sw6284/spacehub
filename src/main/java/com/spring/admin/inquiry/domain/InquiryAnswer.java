package com.spring.admin.inquiry.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import com.spring.client.Inquiry.domain.Inquiry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "spacehub_inquiry_answer")
@SequenceGenerator(name = "spacehub_inquiry_answer_generator", sequenceName = "spacehub_inquiry_answer_seq", initialValue = 1, allocationSize = 1)
public class InquiryAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_inquiry_answer_generator")
    @Column(name = "ans_no")
    private Long ansNo;

    @ManyToOne
    @JoinColumn(name = "inq_no", nullable = false)
    private Inquiry inquiry;

    @Column(name = "adm_no", nullable = false)
    private Long admNo;

    //삭제해도 됨
    @Column(name = "member_id", length = 255)
    private String memberId;
    
    @Lob
    @Column(nullable = false)
    private String answer;

    @UpdateTimestamp
    @Column(name = "ans_date", nullable = false)
    @ColumnDefault(value = "sysdate")
    private LocalDateTime ansDate;

    @UpdateTimestamp
    @Column(name = "ans_update")
    private LocalDateTime ansUpdate;

    @Column(name = "ans_state", length = 10, nullable = false)
    private String ansState;

}
