package com.spring.admin.space.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "spacehub_space")
@SequenceGenerator(name = "space_seq", sequenceName = "space_seq", initialValue = 1, allocationSize = 1)
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "space_seq")
    @Column(name = "sp_no")
    private Long spNo;

    @Column(name = "adm_no", nullable = false)
    private Long admNo;

    @Column(name = "sp_name", nullable = false)
    private String spName;

    @Column(name = "sp_capacity", length = 30, nullable = false)
    private String spCapacity;

    @Column(name = "sp_hour_price", nullable = false)
    private Long spHourPrice;

    @Column(name = "sp_keyword", nullable = false)
    private String spKeyword;

    @Column(name = "sp_main_image")
    private String spMainImage;
}
