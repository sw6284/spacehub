package com.spring.client.reservation.service;

import com.spring.client.reservation.domain.Reservation;

public interface ReservationService {

	public Reservation reservationInsert(Reservation reservation);

	public Reservation resDetail(Long resNo);

}
