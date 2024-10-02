package com.spring.admin.notice.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.admin.auth.service.AdminMypageService;
import com.spring.admin.domain.Admin;
import com.spring.admin.notice.domain.Notice;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import jakarta.servlet.http.HttpSession;

import com.spring.client.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/notice") // 수정된 경로
@RequiredArgsConstructor
public class NoticeAdminController {
    
    private final NoticeService noticeService;
    private final AdminMypageService adminMypageService;

    @GetMapping("/noticeList")
    public String boardList(@RequestParam(value = "page", defaultValue = "1") int page,
                            Model model) {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(page);
        
        PageResponseDTO<Notice> noticeList = noticeService.list(pageRequestDTO);
        model.addAttribute("noticeList", noticeList);
        
        return "admin/notice/noticeList";
    }

    @GetMapping("/insertForm")
    public String insertForm(HttpSession session, Model model, Notice notice) {
    	
    	String admId = (String) session.getAttribute("loggedInAdmin");

        if (admId == null) {
            return "redirect:/admin"; // 로그인 페이지로 리다이렉트
        }

        Admin loggedInAdmin = adminMypageService.getAdminById(admId);
        if (loggedInAdmin != null) {
        	model.addAttribute("admin", loggedInAdmin);
            model.addAttribute("isLoggedIn", true);
            return "admin/notice/noticeInsert";
        } else {
            return "redirect:/admin"; // 로그인 페이지로 리다이렉트
        }
        
    }
    
    @ResponseBody
    @PostMapping("/noticeInsert")
    public String noticeInsert(@RequestBody Notice notice) {
        noticeService.noticeInsert(notice);
        return "/admin/notice/noticeList";
    }
    
    @GetMapping("/updateForm") // 수정된 메서드와 경로
    public String updateForm(@RequestParam("nbNo") Long nbNo, Model model) {
        Notice updateData = noticeService.getNotice(nbNo);
        model.addAttribute("updateData", updateData);
        return "admin/notice/noticeUpdate";
    }

    @PostMapping("/noticeUpdate")
    public String updateNotice(@ModelAttribute Notice notice) {
        notice.setNbUpdate(LocalDateTime.now());
        noticeService.noticeUpdate(notice);
        return "redirect:/admin/notice/noticeList";
    }
 
    @PostMapping("/noticeDelete")
    public String noticeDelete(@RequestParam("nbNo") Long nbNo, RedirectAttributes redirectAttributes) {
        try {
            Notice notice = new Notice();
            notice.setNbNo(nbNo);
            noticeService.noticeDelete(notice);
            redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "공지사항 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/admin/notice/noticeList";
    }
}
