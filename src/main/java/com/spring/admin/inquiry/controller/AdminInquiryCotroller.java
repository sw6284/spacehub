package com.spring.admin.inquiry.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.admin.auth.service.AdminMypageService;
import com.spring.admin.domain.Admin;
import com.spring.admin.inquiry.domain.InquiryAnswer;
import com.spring.admin.inquiry.service.AnswerService;
import com.spring.client.Inquiry.domain.Inquiry;
import com.spring.client.Inquiry.service.InquiryService;

import com.spring.client.domain.Member;
import com.spring.common.vo.PageRequestDTO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/inquiry/*")
@RequiredArgsConstructor
public class AdminInquiryCotroller {

    private final InquiryService inquiryService;
	private final AnswerService answerService;
	private final AdminMypageService adminMypageService;
	
	
	// 문의글 불러오기
   @GetMapping("/inquiryList")
    public String inquiryList(PageRequestDTO pageRequestDTO, Model model) {
    	List<Inquiry> inquiryList = inquiryService.inquiryList();
        model.addAttribute("inquiryList", inquiryList);
        return "admin/inquiry/AnswerInquiryList";
    } 
   
   // 문의글 상세 페이지 
   @GetMapping("/{inqNo}")
   public String inquiryDetail(HttpSession session, @PathVariable Long inqNo, Model model) {
	   String admId = (String) session.getAttribute("loggedInAdmin");

       if (admId == null) {
           return "redirect:/admin"; // 로그인 페이지로 리다이렉트
       }

       Admin loggedInAdmin = adminMypageService.getAdminById(admId);
       if (loggedInAdmin != null) {
    	   Inquiry detail = inquiryService.getInquiry(inqNo);
           model.addAttribute("detail", detail);
           String newLine = System.getProperty("line.separator").toString();
   		   model.addAttribute("newLine", newLine);
           return "admin/inquiry/AnswerinquiryDetail";
       } else {
           return "redirect:/admin"; // 로그인 페이지로 리다이렉트
       }
       
       
   }
   
   @ResponseBody
   @GetMapping(value="/all/{inqNo}", produces=MediaType.APPLICATION_JSON_VALUE)
	public InquiryAnswer inquiryanswerList(@PathVariable Long inqNo, InquiryAnswer inquiryanswer, Inquiry inquiry) {
		inquiry.setInqNo(inqNo);
		inquiryanswer.setInquiry(inquiry);
		InquiryAnswer inquiryanswerList = answerService.inquiryanswerList(inquiryanswer);
		return inquiryanswerList;
	}
   
    @ResponseBody
	@PostMapping(value="/answerInsert", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public InquiryAnswer answerInsert(@RequestBody InquiryAnswer inquiryAnswer) {
		InquiryAnswer result = answerService.inquiryanswerInsert(inquiryAnswer);
		
		Inquiry inquiry = new Inquiry();
		inquiry.setInqNo(inquiryAnswer.getInquiry().getInqNo());
		inquiry.setInqState("답변완료");
		inquiryService.inquiryStateUpdate(inquiry);
		
		return result;
	}
    
    @ResponseBody
    @PutMapping(value="/{ansNo}", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public InquiryAnswer inquiryanswerUpdate(@PathVariable Long ansNo, @RequestBody InquiryAnswer inquiryAnswer, Inquiry inquiry) {
    	inquiryAnswer.setAnsNo(ansNo);
    	InquiryAnswer result = answerService.inquiryanswerUpdate(inquiryAnswer);
    	return result;
    }
    
    @ResponseBody
	@DeleteMapping(value="/{inqNo}/{ansNo}", produces =  MediaType.TEXT_PLAIN_VALUE)
	public void inquiryanswerDelete(@PathVariable Long inqNo, @PathVariable Long ansNo, InquiryAnswer inquiryAnswer) {
		inquiryAnswer.setAnsNo(ansNo);
		answerService.inquiryanswerDelete(inquiryAnswer); 
		
		Inquiry inquiry = new Inquiry();
		inquiry.setInqNo(inqNo);
		inquiry.setInqState("답변대기");
		inquiryService.inquiryStateUpdate(inquiry);
		
	}
}

