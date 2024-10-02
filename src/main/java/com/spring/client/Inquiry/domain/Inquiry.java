package com.spring.client.Inquiry.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import com.spring.client.domain.Member;

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
@Table(name = "spacehub_inquiry")
@SequenceGenerator(name = "spacehub_inquiry_generator", sequenceName = "spacehub_inquiry_seq", initialValue = 1, allocationSize = 1)
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_inquiry_generator")
    @Column(name = "inq_no")
    private Long inqNo;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(name = "inq_cate", length = 10, nullable = false)
    private String inqCate;

    @Column(name = "inq_title", length = 50, nullable = false)
    private String inqTitle;

    @UpdateTimestamp
    @Column(name = "inq_date", nullable = false)
    @ColumnDefault(value = "sysdate")
    private LocalDateTime inqDate;

    @UpdateTimestamp
    @Column(name = "inq_update")
    private LocalDateTime inqUpdate;

    
    /* 게시글 상태(작성중/작성완료) : 작성중 아예 없앰 */
    @Column(name = "inq_state", length = 15, nullable = false)
    private String inqState = "답변대기"; 
    
    /*
    @Column(name = "inq_state", length = 15, nullable = false)
    private String inqState = "default_state";  // 기본값 설정 */

    //@Lob									
    @Column(length = 1000, nullable = false)
    private String inqContent;

    @Column(name = "inq_data")
    private String inqData;

    @Column(name = "inq_secret", nullable = false)
    private boolean inqSecret;

    @Column(name = "inq_passwd", length = 100)
    private String inqPassword;
}