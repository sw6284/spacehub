package com.spring.client.notice.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.admin.notice.domain.Notice;
import com.spring.client.notice.service.NoticeService;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/notice/*")
@RequiredArgsConstructor
public class NoticeClientController {
	
	private final NoticeService noticeService;
	

    @GetMapping("/noticeList")
    public String boardList(@RequestParam(value = "page", defaultValue = "1") int page,
                            Model model) {
        // PageRequestDTO 설정
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(page);
        
        // 공지사항 목록 조회
        PageResponseDTO<Notice> noticeList = noticeService.list(pageRequestDTO);
        model.addAttribute("noticeList", noticeList);
        
        return "client/notice/noticeList";
    }

	
	@GetMapping("/insertForm")
	public String insertForm(Notice notice) {
		
		return "client/notice/noticeInsert";
	}
	
	@PostMapping("/noticeInsert")
	public String noticeInsert(@RequestBody Notice notice) {
		noticeService.noticeInsert(notice);
		
		return "redirect:/admin/notice/noticeList";
	}
	
	@PostMapping("/updateForm")
	public String updateForm(@ModelAttribute Notice notice, Model model) {
	    // 해당 공지사항 데이터를 가져옴
	    Notice updateData = noticeService.getNotice(notice.getNbNo());

	    // 모델에 updateData 추가
	    model.addAttribute("updateData", updateData);

	    // 수정 페이지로 이동
	    return "client/notice/noticeUpdate"; // 이 경로가 맞는지 확인
	}

	 @PostMapping("/noticeUpdate")
	    public String updateNotice(@RequestParam("nbNo") Long nbNo,
	                               @RequestParam("nbTitle") String nbTitle,
	                               @RequestParam("admNo") String admNo,
	                               @RequestParam("nbContent") String nbContent,
	                               Model model) {
	        // Notice 객체 생성
	        Notice notice = new Notice();
	        notice.setNbNo(nbNo);
	        notice.setNbTitle(nbTitle);
	        notice.setAdmNo(admNo);
	        notice.setNbContent(nbContent);
	        notice.setNbUpdate(LocalDateTime.now()); // 수정 날짜 설정
	        
	        // 서비스 호출
	        noticeService.noticeUpdate(notice);

	        // 수정 후 목록 페이지로 리다이렉트
	        return "redirect:/notice/noticeList";
	    }
	 
	 @PostMapping("/notice/noticeDelete")
	    public String noticeDelete(@RequestParam("nbNo") Long nbNo, RedirectAttributes redirectAttributes) {
	        try {
	            Notice notice = new Notice();
	            notice.setNbNo(nbNo); // nbNo로 공지사항을 찾아 삭제
	            noticeService.noticeDelete(notice);
	            redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 삭제되었습니다.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            redirectAttributes.addFlashAttribute("error", "공지사항 삭제 중 오류가 발생했습니다.");
	        }
	        return "redirect:/notice/noticeList"; // 삭제 후 공지사항 목록 페이지로 리다이렉트
	    }

}

