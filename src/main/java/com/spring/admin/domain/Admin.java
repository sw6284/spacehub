package com.spring.admin.domain;

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
	@Column(name="adm_name", length = 20, nullable = false)
	private String admName;
	
	/* 관리자 아이디 */
	@Column(name="adm_id",length = 20, nullable = false)
	private String admId;
	
	/* 관리자 비밀번호 */
	@Column(name="adm_passwd",length= 15, nullable=false)
	private String admPasswd;
	
	/* 관리자 이메일 */
	@Column(name="adm_email",length=30)
	private String admEmail;
	
	/* 사용자 상태 */
	@Column(name="adm_state",length=10, nullable=false)
	private String admState = "active";
	

}
