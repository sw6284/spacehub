package com.spring.client.reservation.service;

import java.util.List;

import com.spring.client.reservation.domain.Reservation;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;
import com.spring.common.vo.SearchRequestDTO;

public interface ReservationService {

	public Reservation reservationInsert(Reservation reservation);

	public Reservation resDetail(Long resNo);

	public List<Reservation> getByMemberNo(Long memberNo);
	public boolean isReservationExists(Long resNo);

	public void cancelScheduledDeletion(Long resNo);

	public List<Reservation> reservationList();

	public PageResponseDTO<Reservation> getReservationList(PageRequestDTO pageRequestDTO);

	public PageResponseDTO<Reservation> searchReservations(SearchRequestDTO searchRequestDTO, PageRequestDTO pageRequestDTO);
	
}
