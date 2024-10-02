package com.spring.client.auth.controller;

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

import com.spring.client.auth.service.UserMypageService;
import com.spring.client.domain.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class UserMypageController {

    private final UserMypageService userMypageService;

    /**
     * 개인정보 확인 페이지 반환
     * @param session HTTP 세션
     * @param model 모델 객체
     * @return 개인정보 페이지 뷰 이름
     */
    @GetMapping
    public String myPage(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("loggedInUser");

        if (memberId == null) {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }

        Member loggedInUser = userMypageService.getMemberById(memberId);
        if (loggedInUser != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = loggedInUser.getMemberCreatedAt().format(formatter);

            model.addAttribute("member", loggedInUser);
            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("isLoggedIn", true);

            return "client/mypage/userProfile";
        } else {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
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
        String memberId = (String) session.getAttribute("loggedInUser");
        if (memberId == null) {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }

        Member loggedInUser = userMypageService.getMemberById(memberId);
        if (loggedInUser != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = loggedInUser.getMemberCreatedAt().format(formatter);

            model.addAttribute("member", loggedInUser);
            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("isLoggedIn", true);
            return "client/mypage/userUpdateForm";
        } else {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }
    }
    
    /**
     * 개인정보 업데이트 처리
     * @param session HTTP 세션
     * @param member 업데이트할 개인정보
     * @return 업데이트 결과 메시지
     */
    @PostMapping("/updateMember")
    public ResponseEntity<String> updateMember(HttpSession session, @RequestBody Member member) {
        String memberId = (String) session.getAttribute("loggedInUser");

        if (memberId != null) {
            Member existingMember = userMypageService.getMemberById(memberId);

            if (existingMember != null) {
                existingMember.setMemberName(member.getMemberName());
                existingMember.setMemberPhone(member.getMemberPhone());
                existingMember.setMemberEmail(member.getMemberEmail());

                if (member.getMemberPassword() != null && !member.getMemberPassword().isEmpty()) {
                    existingMember.setMemberPassword(member.getMemberPassword());
                }

                boolean isUpdated = userMypageService.updateMember(existingMember);
                if (isUpdated) {
                    return ResponseEntity.ok("UPDATED"); // 업데이트 성공
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAILED"); // 업데이트 실패
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER_NOT_FOUND"); // 사용자 없음
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
        return "client/mypage/checkPassword"; // 비밀번호 확인 페이지로 이동
    }

    /**
     * 비밀번호 확인 처리
     * @param session HTTP 세션
     * @param password 입력된 비밀번호
     * @return 비밀번호 확인 결과 메시지
     */
    @PostMapping("/confirmPassword")
    public ResponseEntity<String> confirmPassword(HttpSession session, @RequestParam("password") String password) {
        String memberId = (String) session.getAttribute("loggedInUser");

        if (memberId != null) {
            boolean isPasswordCorrect = userMypageService.checkPassword(memberId, password);
            if (isPasswordCorrect) {
                return ResponseEntity.ok("OK"); // 비밀번호 확인 성공
            } else {
                return ResponseEntity.status(401).body("ERROR"); // 비밀번호 확인 실패
            }
        } else {
            return ResponseEntity.status(401).body("ERROR"); // 로그인 세션 없음
        }
    }

    /**
     * 회원 탈퇴 폼 페이지 반환
     * @return 회원 탈퇴 폼 페이지 뷰 이름
     */
    @GetMapping("/userDeleteForm")
    public String userDeleteForm() {
        return "client/mypage/userDeleteForm"; // 회원 탈퇴 폼 페이지로 이동
    }

    /**
     * 회원 탈퇴 처리
     * @param session HTTP 세션
     * @return 탈퇴 결과 메시지
    */
    @PostMapping("/deleteMember")
    public ResponseEntity<String> deleteMember(HttpSession session) {
        String memberId = (String) session.getAttribute("loggedInUser");

        if (memberId != null) {
            userMypageService.nullifyUserData(memberId); // 사용자의 모든 정보를 NULL로 설정
            session.invalidate(); // 세션 무효화
            return ResponseEntity.ok("DELETED"); // 삭제 성공
        } else {
            return ResponseEntity.status(401).body("ERROR"); // 로그인 세션 없음
        }
    }
    
    /*
    @GetMapping("/myInquiryList")
	public String myInquiryList(HttpSession session, PageRequestDTO pageRequestDTO, Model model) {
	    String memberId = (String) session.getAttribute("loggedInUser");
	
	    if (memberId == null) {
	        return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
	    }
	
	    // 회원의 ID로 회원 번호를 조회 (이 방법이 아니라면 memberId로 직접 조회할 수 있는 방법도 고려)
	    Member loggedInUser = userMypageService.getMemberById(memberId);
	    if (loggedInUser == null) {
	        return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
	    }
	
	    // 문의글 조회
	    PageResponseDTO<Inquiry> inquiryList = userMypageService.getInquiriesByMemberNo(loggedInUser.getMemberNo(), pageRequestDTO);
	    model.addAttribute("inquiryList", inquiryList);
	    
	    return "client/mypage/myInquiryList";
	}
*/
}