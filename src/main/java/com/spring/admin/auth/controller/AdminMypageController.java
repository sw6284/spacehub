package com.spring.admin.auth.controller;

import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.admin.auth.service.AdminMypageService;
import com.spring.admin.domain.Admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/myPage")
public class AdminMypageController {

    private final AdminMypageService adminMypageService;

    /**
     * 개인정보 확인 페이지 반환
     * @param session HTTP 세션
     * @param model 모델 객체
     * @return 개인정보 페이지 뷰 이름
     */
    @GetMapping
    public String myPage(HttpSession session, Model model) {
        String admId = (String) session.getAttribute("loggedInAdmin");

        if (admId == null) {
            return "redirect:/admin"; // 로그인 페이지로 리다이렉트
        }

        Admin loggedInAdmin = adminMypageService.getAdminById(admId);
        if (loggedInAdmin != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = loggedInAdmin.getAdmCreatedAt().format(formatter);

            model.addAttribute("admin", loggedInAdmin);
            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("isLoggedIn", true);

            return "admin/mypage/adminProfile";
        } else {
            return "redirect:/admin"; // 로그인 페이지로 리다이렉트
        }
    }

    /**
     * 개인정보 수정 폼 페이지 반환
     * @param session HTTP 세션
     * @param model 모델 객체
     * @return 개인정보 수정 페이지 뷰 이름
     */
    @GetMapping("/updateForm")
    public String myPageUpdateForm(HttpSession session, Model model) {
        String adminId = (String) session.getAttribute("loggedInAdmin");
        if (adminId == null) {
            return "redirect:/admin"; // 로그인 페이지로 리다이렉트
        }

        Admin loggedInAdmin = adminMypageService.getAdminById(adminId);
        if (loggedInAdmin != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = loggedInAdmin.getAdmCreatedAt().format(formatter);

            model.addAttribute("admin", loggedInAdmin);
            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("isLoggedIn", true);
            return "admin/mypage/adminUpdateForm";
        } else {
            return "redirect:/admin"; // 로그인 페이지로 리다이렉트
        }
    }

    /**
     * 개인정보 업데이트 처리
     * @param session HTTP 세션
     * @param admin 업데이트할 개인정보
     * @return 업데이트 결과 메시지
     */
    @PostMapping("/updateAdmin")
    public ResponseEntity<String> updateAdmin(HttpSession session, @RequestBody Admin admin) {
        String adminId = (String) session.getAttribute("loggedInAdmin");

        if (adminId != null) {
            Admin existingAdmin = adminMypageService.getAdminById(adminId);

            if (existingAdmin != null) {
                existingAdmin.setAdmName(admin.getAdmName());
                existingAdmin.setAdmPhone(admin.getAdmPhone());

                if (admin.getAdmPasswd() != null && !admin.getAdmPasswd().isEmpty()) {
                    existingAdmin.setAdmPasswd(admin.getAdmPasswd());
                }

                boolean isUpdated = adminMypageService.updateAdmin(existingAdmin);
                if (isUpdated) {
                    return ResponseEntity.ok("UPDATED"); // 업데이트 성공
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAILED"); // 업데이트 실패
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ADMIN_NOT_FOUND"); // 관리자 정보 없음
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NOT_LOGGED_IN"); // 로그인되지 않음
        }
    }

    /**
     * 비밀번호 확인 페이지 반환
     * @return 비밀번호 확인 페이지 뷰 이름
     */
    @GetMapping("/checkPassword")
    public String checkPwd() {
        return "admin/mypage/checkPassword"; // 비밀번호 확인 페이지로 이동
    }

    /**
     * 비밀번호 확인 처리
     * @param session HTTP 세션
     * @param password 입력된 비밀번호
     * @return 비밀번호 확인 결과 메시지
     */
    @PostMapping("/confirmPassword")
    public ResponseEntity<String> confirmPassword(HttpSession session, @RequestParam("password") String password) {
        String adminId = (String) session.getAttribute("loggedInAdmin");

        if (adminId != null) {
            boolean isPasswordCorrect = adminMypageService.checkPassword(adminId, password);
            if (isPasswordCorrect) {
                return ResponseEntity.ok("OK"); // 비밀번호 확인 성공
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR"); // 비밀번호 확인 실패
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ERROR"); // 로그인 세션 없음
        }
    }
}