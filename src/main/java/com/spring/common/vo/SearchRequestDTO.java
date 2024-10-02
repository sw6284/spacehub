package com.spring.common.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequestDTO {

	private String searchType;
	private String keyWord;
	private LocalDateTime dateSearch;
}
