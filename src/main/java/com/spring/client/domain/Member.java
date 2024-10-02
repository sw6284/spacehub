package com.spring.client.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SPACEHUB_MEMBER")
@SequenceGenerator(name = "spacehub_member_generator", sequenceName = "spacehub_member_seq", initialValue = 4, allocationSize = 1)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_member_generator")
    @Column(name = "member_no")
    private Long memberNo;

    @Column(name = "member_name", length = 20, nullable = false)
    private String memberName;

    @Column(name = "member_id", length = 15)
    private String memberId;

    @Column(name = "member_passwd", length = 15)
    private String memberPassword;

    @Column(name = "member_email", length = 30)
    private String memberEmail;

    @Column(name = "member_phone", length = 15)
    private String memberPhone;

    @Column(name = "member_state", length = 10, nullable = false)
    private String memberState = "active";

    @Column(name = "kakao_id", length = 20)
    private String kakaoId;

    @Column(name = "kakao_connect_at")
    private LocalDateTime kakaoConnectAt;

    @CreationTimestamp
    @Column(name = "member_created_at")
    @ColumnDefault(value = "sysdate")
    private LocalDateTime memberCreatedAt;

    @Column(name = "member_update_at")
    private LocalDateTime memberUpdateAt;
}
