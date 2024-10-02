package com.spring.admin.space.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Entity
@Table(name = "spacehub_space_detail")
@SequenceGenerator(name = "space_detail_seq", sequenceName = "space_detail_seq", initialValue = 1, allocationSize = 1)
public class SpaceDetail {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "space_detail_seq")
    @Column(name = "sp_detail")
	private Long spDetail;	// 상세정보 번호

	@Column(name = "sp_description")
	private String spDescription;	// 공간 상세 설명
	
	@Column(name = "sp_equipment")
	private String spEquipment;		// 비치된 물품
	
	@Column(name = "sp_rules")
	private String spRules;			// 이용 수칙
	
	@Column(name = "sp_start_time")
	private String spStartTime;		// 가능 시작 시간
	
	@Column(name = "sp_end_time")
	private String spEndTime;		// 종료 시간 
	
	@OneToOne
	@JoinColumn(name = "sp_no", referencedColumnName = "sp_no")
    private Space space; // 공간 번호 (Foreign Key)
	
	@OneToMany(mappedBy = "spaceDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpaceImg> spaceImgs; // SpaceImg와의 관계
}
