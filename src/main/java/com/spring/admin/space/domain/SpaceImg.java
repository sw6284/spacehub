package com.spring.admin.space.domain;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "spacehub_space_img")
@SequenceGenerator(name = "space_img_seq", sequenceName = "space_img_seq", initialValue = 1, allocationSize = 1)
public class SpaceImg {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "space_img_seq")
	@Column(name = "api_no")
	private Long apiNo;		// 이미지 식별번호

	@Column(name = "sp_img")
	private String spImg;	// 공간 이미지 이름, 결오
	
	@Column(name = "is_main")
	private Boolean isMain;	// 대표이미지 여부 (private boolean isMain = false;  // 기본값을 false로 설정)
	
	@Transient
	private MultipartFile file;		// 파일 업로드를 위한 필드	
	
	@ManyToOne
	@JoinColumn(name = "sp_detail", referencedColumnName = "sp_detail")
    private SpaceDetail spaceDetail; // 상세정보 번호 (Foreign Key)
}
