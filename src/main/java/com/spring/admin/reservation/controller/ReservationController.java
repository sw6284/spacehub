package com.spring.admin.reservation.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.client.reservation.domain.Reservation;
import com.spring.client.reservation.service.ReservationService;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;
import com.spring.common.vo.SearchRequestDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller("adminReservationController")
@RequestMapping("/admin/*")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@GetMapping("/reservationManager")
	public String getReservationManager(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(value = "searchType", required = false) String searchType,
	        @RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "date", required = false) LocalDateTime date,
	        Model model) {

	    // 검색 조건을 담을 DTO 생성
	    SearchRequestDTO searchRequestDTO = new SearchRequestDTO();
	    searchRequestDTO.setKeyWord(keyword);
	    
	    if (date != null) {
	        searchRequestDTO.setDateSearch(LocalDateTime.parse(date + "T00:00:00"));
	    }else {searchRequestDTO.setDateSearch(date);}
	    searchRequestDTO.setSearchType(searchType);
	    
	    // PageRequestDTO 빌드
	    PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();

	    // 서비스에서 페이지 응답 DTO 받아오기
	    PageResponseDTO<Reservation> pageResponseDTO = reservationService.searchReservations(searchRequestDTO, pageRequestDTO);

	    // 모델에 페이지 응답 DTO 추가
	    model.addAttribute("reservations", pageResponseDTO.getDtoList());
	    model.addAttribute("pageResponse", pageResponseDTO);

	    return "admin/respayManager/reservation";
	}
	
	
	@GetMapping("/reservationManagerSearch")
	public String getReservationManagerSearch(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(value = "searchType", required = false) String searchType,
	        @RequestParam(value = "keyword", required = false) String keyword,
	        @RequestParam(value = "date", required = false) LocalDateTime date,
	        Model model) {

	    // 검색 조건을 담을 DTO 생성
	    SearchRequestDTO searchRequestDTO = new SearchRequestDTO();
	    searchRequestDTO.setKeyWord(keyword);
	    
	    if (date != null) {
	        searchRequestDTO.setDateSearch(LocalDateTime.parse(date + "T00:00:00"));
	    }else {searchRequestDTO.setDateSearch(date);}
	    searchRequestDTO.setSearchType(searchType);
	    
	    // PageRequestDTO 빌드
	    PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(page).size(size).build();

	    // 서비스에서 페이지 응답 DTO 받아오기
	    PageResponseDTO<Reservation> pageResponseDTO = reservationService.searchReservations(searchRequestDTO, pageRequestDTO);

	    // 모델에 페이지 응답 DTO 추가
	    model.addAttribute("reservations", pageResponseDTO.getDtoList());
	    model.addAttribute("pageResponse", pageResponseDTO);

	    return "admin/respayManager/reservationTable";
	}
}
	