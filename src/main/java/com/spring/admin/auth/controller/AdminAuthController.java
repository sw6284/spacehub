package com.spring.admin.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.admin.auth.service.AdminAuthService;
import com.spring.admin.domain.Admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminAuthController {
	@Autowired
	private AdminAuthService adminAuthService;
	
    @GetMapping
    public String adminLoginForm() {
        return "admin/auth/adminLoginForm"; 
    }
    
    @PostMapping("/adminLogin")
    @ResponseBody
    public Map<String, String> adminLogin(@RequestBody Admin loginRequest, HttpSession session) {
        Admin admin = adminAuthService.adminLogin(loginRequest.getAdmId(), loginRequest.getAdmPasswd());
        Map<String, String> response = new HashMap<>();
        if (admin != null) {
            session.setAttribute("loggedInAdmin", admin.getAdmId());
            response.put("message", "Login successful");
        } else {
            response.put("message", "로그인 실패");
        }
        return response;
    }
    
    @GetMapping("/isLoggedIn")
    @ResponseBody
    public ResponseEntity<Boolean> isLoggedIn(HttpSession session) {
        String loggedInAdmin = (String) session.getAttribute("loggedInAdmin");
        boolean loggedIn = loggedInAdmin != null;
        return ResponseEntity.ok(loggedIn);
    }
    
    @GetMapping("/getAdminInfo")
    @ResponseBody
    public ResponseEntity<Admin> getAdminInfo(HttpSession session) {
        String loggedInAdmin = (String) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin != null) {
            Admin admin = adminAuthService.getAdminById(loggedInAdmin);
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); 
        return "redirect:/admin"; 
    }
    
    /**
     * 회원 탈퇴 처리
     * @param session HTTP 세션
     * @return 탈퇴 결과 메시지
    */
    @PostMapping("/deleteAdmin")
    public ResponseEntity<String> deleteAdmin(@RequestParam("admId") String admId) {
        if (admId != null) {
        	adminAuthService.nullifyAdminData(admId); // 관리자 정보 null로 설정
        	return ResponseEntity.ok("DELETED"); // 삭제 성공
        } else {
            return ResponseEntity.status(401).body("ERROR"); // 로그인 세션 없음
        }
    }
    
}
