package com.spring.client.Inquiry.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.client.Inquiry.domain.Inquiry;
import com.spring.client.Inquiry.service.InquiryService;
import com.spring.client.auth.service.UserAuthService;
import com.spring.client.domain.Member;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/inquiry/*")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserAuthService userAuthService;
  
    
    @GetMapping("/inquiryList")
    public String inquiryList(PageRequestDTO pageRequestDTO, Model model) {
    	List<Inquiry> inquiryList = inquiryService.inquiryList();
        model.addAttribute("inquiryList", inquiryList);
        return "client/inquiry/inquiryList";
    } 
    /*
    @GetMapping("/inquiryList")
    public String inquiryList(Inquiry inquiry, PageRequestDTO pageRequestDTO, Model model) {
    	PageResponseDTO<Inquiry> inquriyList = inquiryService.list(pageRequestDTO);;
        model.addAttribute("inquiryList", inquriyList);
        return "client/inquiry/inquiryList";
    }*/

    @GetMapping("/insertForm")
    public String insertForm(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("loggedInUser");

        if (memberId == null) {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }

        Member loggedInUser = userAuthService.getMemberById(memberId);
        if (loggedInUser != null) {
            Inquiry inquiry = new Inquiry();
            inquiry.setMember(loggedInUser);
            model.addAttribute("inquiry", inquiry); // inquiry 객체를 모델에 추가
            return "client/inquiry/inquiryInsertForm";
        } else {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }
    }

  
   // 입력 처리
    @PostMapping("/insert")
    public String saveInquiry(@RequestBody Inquiry inquiry) {
        inquiryService.saveInquiry(inquiry);  // Inquiry 엔티티로 직접 처리
        return "redirect:/inquiry/inquiryList";
    }

    @GetMapping("/{inqNo}")
    public String inquiryDetail(HttpSession session, @PathVariable("inqNo") Long inqNo, Model model) {
    	String memberId = (String) session.getAttribute("loggedInUser");
    	
    	if (memberId == null) {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }

        Member loggedInUser = userAuthService.getMemberById(memberId);
        if (loggedInUser != null) {
	    	Inquiry detail = inquiryService.getInquiry(inqNo);
	        model.addAttribute("detail", detail);
	        String newLine = System.getProperty("line.separator").toString();
			model.addAttribute("newLine", newLine);
			model.addAttribute("loggedInMemberNo", loggedInUser.getMemberNo());
			return "client/inquiry/inquiryDetail";
        } else {
            return "redirect:/auth/login"; // 로그인 페이지로 리다이렉트
        }
    }

    // 수정 폼 이동
    @GetMapping("/updateForm/{inqNo}")
    public String updateForm(@PathVariable("inqNo") Long inqNo, Model model) {
        Inquiry inquiry = inquiryService.getInquiry(inqNo); // 수정할 문의글 정보 가져오기
        model.addAttribute("inqUpdate", inquiry); // 수정 폼에 전달할 Inquiry 객체 추가
        return "client/inquiry/updateForm"; // 수정 폼으로 이동
    }


    
    // 수정 처리
    @PostMapping("/update")
    public String updateInquiry(@ModelAttribute Inquiry inquiry) {
        System.out.println("Received Inquiry: " + inquiry);

        Inquiry existingInquiry = inquiryService.getInquiry(inquiry.getInqNo()); // 기존 문의글 가져오기

        if (existingInquiry != null) {
            // 기존 문의글 업데이트
            existingInquiry.setInqTitle(inquiry.getInqTitle()); // 제목 수정
            existingInquiry.setInqContent(inquiry.getInqContent()); // 내용 수정
            existingInquiry.setInqSecret(inquiry.isInqSecret()); // 비밀글 여부 수정
            existingInquiry.setInqPassword(inquiry.getInqPassword()); // 비밀번호 수정
            
            // 업데이트된 문의글 저장
            inquiryService.saveInquiry(existingInquiry);
            return "redirect:/inquiry/inquiryList"; // 수정 후 목록 페이지로 리다이렉트
        } else {
            return "redirect:/inquiry/inquiryList"; // 수정 실패 시에도 목록 페이지로 리다이렉트
        }
    }


    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> inquiryDelete(@RequestParam("inqNo") Long inqNo) {
        try {
            boolean deleted = inquiryService.inquiryDelete(inqNo); // 삭제 작업 수행

            if (deleted) {
                // 삭제 성공 시
                return ResponseEntity.ok("문의글이 삭제되었습니다.");
            } else {
                // 삭제 실패 시 (레코드가 존재하지 않거나 다른 이유로 삭제 실패)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("문의글을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            // 예외 발생 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문의글 삭제 중 오류가 발생했습니다.");
        }
    }
}



