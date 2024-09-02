package com.spring.client.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.client.reservation.domain.Reservation;

@Controller
public class MainController {

	@GetMapping(value="/")
	public String main(Model model) {
		 model.addAttribute("reservation", new Reservation());
		return "client/main";
	}
	
}
