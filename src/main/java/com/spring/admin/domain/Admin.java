package com.spring.admin.domain;

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
@Table(name = "SPACEHUB_ADMIN")
@SequenceGenerator(name = "spacehub_admin_generator", sequenceName = "spacehub_admin_seq", initialValue = 1, allocationSize = 1)
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_admin_generator")
	private Long admNo;
	
	/* 객체 필드를 테이블 컬럼에 매핑 */
	/* 관리자 이름 */
	@Column(name="adm_name", length = 20)
	private String admName;
	
	/* 관리자 아이디 */
	@Column(name="adm_id",length = 20)
	private String admId;
	
	/* 관리자 비밀번호 */
	@Column(name="adm_passwd",length= 15)
	private String admPasswd;
	
	/* 관리자 이메일 */
	@Column(name="adm_email",length=30)
	private String admEmail;
	
	/* 관리자 상태 */
	@Column(name="adm_state",length=10, nullable=false)
	private String admState = "active";
	
	/* 관리자 전화번호 */
	@Column(name = "adm_phone", length = 15)
    private String admPhone;
	
	/* 관리자 등록일 */
	@CreationTimestamp
    @Column(name = "adm_created_at")
    @ColumnDefault(value = "sysdate")
    private LocalDateTime admCreatedAt;

	/* 관리자 삭제일 */
    @Column(name = "adm_update_at")
    private LocalDateTime admUpdateAt;
    
    /* 최고권한 여부 */
    @Column(name = "adm_super", length = 1)
    private String admSuper = "N";
}
