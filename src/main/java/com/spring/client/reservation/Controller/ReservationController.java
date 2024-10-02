package com.spring.client.reservation.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.admin.space.domain.Space;
import com.spring.admin.space.repository.SpaceRepository;
import com.spring.client.auth.service.UserMypageService;
import com.spring.client.domain.Member;
import com.spring.client.reservation.domain.Reservation;
import com.spring.client.reservation.service.ReservationService;
import com.spring.payment.vo.ReservationDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller("clientReservationController")
@RequestMapping("/reservation/*")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

	private final ReservationService reservationService;
	private final UserMypageService userMypageService;
	private final SpaceRepository spaceRepository;
	@GetMapping("/resList")
	public String resDetail(HttpSession session, Model model) {
		String memberId = (String) session.getAttribute("loggedInUser");
		Map<Long, String> endTimeMap = new HashMap<>();
		Map<Long, Boolean> isCancellableMap = new HashMap<>(); // 취소 가능 여부 저장

		// 로그인을 확인하는 코드 (이 부분을 사용하려면 주석 해제)
		if (memberId == null) {
			return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
		}
		
		//예약 리스트 가져오기(회원 번호 사용)
		Member loggedInUser = userMypageService.getMemberById(memberId);
		List<Reservation> reservations = reservationService.getByMemberNo(loggedInUser.getMemberNo());;

		// 예약 리스트 가져오기 (테스트용 회원 번호 1번 사용)
		//Long no = (long) 1;
		//List<Reservation> reservations = reservationService.getByMemberNo(no);

		// 각 예약에 대해 퇴실 시간 및 취소 가능 여부 계산
		for (Reservation reservation : reservations) {
			LocalDateTime resStartTime = reservation.getResStartTime();
			int resUseTime = reservation.getResUseTime();

			// 퇴실 시간 계산 (시작 시간에 사용 시간을 더함)
			LocalDateTime resEndTime = resStartTime.plusHours(resUseTime);

			// 퇴실 시간을 원하는 형식으로 포맷
			String formattedEndTime = resEndTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			endTimeMap.put(reservation.getResNo(), formattedEndTime);

			// 현재 시간이 입실 시간 이전인지 여부 확인
			boolean isCancellable = LocalDateTime.now().isBefore(resStartTime);
			isCancellableMap.put(reservation.getResNo(), isCancellable); // 취소 가능 여부 저장
		}

		// 모델에 예약 정보와 함께 endTime 및 취소 가능 여부 전달
		model.addAttribute("reservation", reservations);
		model.addAttribute("endTimeMap", endTimeMap);
		model.addAttribute("isCancellableMap", isCancellableMap); // 취소 가능 여부 전달

		log.info("res=" + reservations);
		return "client/payment/reservationList"; // Thymeleaf 템플릿 파일로 이동

	}

	@PostMapping("/orders")
	@ResponseBody
	public String orderSave(@RequestBody ReservationDTO reservationDTO, HttpSession session) {
		Reservation reservation = new Reservation();
		reservation.setResNo(reservationDTO.getResNo());
		String memberId = (String) session.getAttribute("loggedInUser");
		
		if (memberId == null) {
			return "/auth/login"; // 로그인 페이지로 리다이렉트
		}
		
		if(reservation.getResNo()!= null) {
			// 예약 테이블에 해당 예약 번호가 존재하는지 확인
			boolean reservationExists = reservationService.isReservationExists(reservation.getResNo());
			
			// 예약이 이미 존재하면 바로 리다이렉트
			if (reservationExists) {
				// resNo를 Base64로 인코딩
				String encodedResNo = Base64.getEncoder().encodeToString(reservation.getResNo().toString().getBytes());

				// 기존 예약이 존재하는 경우 해당 예약의 결제 페이지로 리다이렉트
				return "/reservation/orders/payment/" + encodedResNo;
			}
			}
		
		Member member = userMypageService.getMemberById(memberId);
	
		Space space = spaceRepository.findBySpNo(reservationDTO.getSpNo());
		
	    reservation.setSpNo(space);
	    reservation.setResDate(reservationDTO.getResDate());
	    reservation.setResStartTime(reservationDTO.getResDate());
	    reservation.setResUseTime(reservationDTO.getResUseTime());
	    reservation.setResPersonnel(reservationDTO.getResPersonnel());
	    reservation.setResAmount(reservationDTO.getResAmount());
	    reservation.setResState("결제대기");
	    reservation.setMember(member);
		log.info("------------------------------------------------------------------------------------------resDate: " + reservation);
		
		
		
		
		// 예약 정보를 저장
		Reservation savedReservation = reservationService.reservationInsert(reservation);

		// resNo를 Base64로 인코딩
		String encodedResNo = Base64.getEncoder().encodeToString(savedReservation.getResNo().toString().getBytes());

		// 인코딩된 resNo를 URL 경로에 포함시켜 리다이렉트
		return "/reservation/orders/payment/" + encodedResNo;
	}

	
	
	@GetMapping("/orders/payment/{resNo}")
	public String paymentDetail(@PathVariable("resNo") String encodedResNo, Model model) {
		// Base64로 인코딩된 resNo를 디코딩
		Long resNo = Long.parseLong(new String(Base64.getDecoder().decode(encodedResNo)));
		// Long resNo = (long) 15;

		// 예약 번호를 이용해 예약 정보를 가져옴
		Reservation reservation = reservationService.resDetail(resNo);
		Member member = userMypageService.getMemberById(reservation.getMember().getMemberId());

		// 퇴실시간계산
		LocalDateTime resStartTime = reservation.getResStartTime();

		// 퇴실 시간 계산 (시작 시간에 사용 시간을 더함)
		LocalDateTime resEndTime = resStartTime.plusHours(reservation.getResUseTime());

		// 퇴실 시간을 원하는 형식으로 포맷
		String formattedEndTime = resEndTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (EEEE) HH:mm"));
		String endTime = (formattedEndTime);

		// 모델에 예약 정보를 추가
		model.addAttribute("memberName", member.getMemberName());
		model.addAttribute("memberPhone", member.getMemberPhone());
		model.addAttribute("reservation", reservation);
		model.addAttribute("endTime", endTime);
		log.info("res = " + reservation);
		log.info("member=" + member);
		// 결제 상세 페이지로 이동
		return "client/payment/payment";
	}


	
	
}