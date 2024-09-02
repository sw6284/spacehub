package com.spring.admin.notice.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "SPACEHUB_NOTICE")
@SequenceGenerator(name = "spacehub_notice_generator", sequenceName = "spacehub_notice_seq", initialValue = 1, allocationSize = 1)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spacehub_notice_generator")
    @Column(name = "nb_no")
    private Long nbNo;

    @Column(name = "nb_title", length = 255, nullable = false)
    private String nbTitle;

    @Column(name = "adm_no", length = 100, nullable = false)
    private String admNo;

    @Column(name = "nb_content", length = 255, nullable = false)
    private String nbContent;

    @Column(name = "nb_cate", length = 20, nullable = false)
    private String nbCate;

    @UpdateTimestamp
    @Column(name = "nb_date", nullable = false)
    private LocalDateTime nbDate;

    @Column(name = "nb_update")
    private LocalDateTime nbUpdate;

    @Column(name = "nb_hit", nullable = false)
    @ColumnDefault(value = "0")
    private int nbHit = 0;
}
