package com.spring.payment.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
	  private Long resNo;
	    private Long spNo;
	    private LocalDateTime resDate;
	    private LocalDateTime resStartTime;
	    private int resUseTime;
	    private int resPersonnel;
	    private int resAmount;
	    private String resState;
}
