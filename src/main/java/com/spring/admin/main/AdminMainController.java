package com.spring.admin.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 관리자
@Controller
@RequestMapping("/admin/*")
public class AdminMainController {
	
	@GetMapping("/main")
	public String mainAdmin() {
		return "admin/adminMain";
	}
	
}
