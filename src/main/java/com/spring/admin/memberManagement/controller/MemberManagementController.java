package com.spring.admin.memberManagement.controller;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.admin.memberManagement.service.MemberManagementService;
import com.spring.client.auth.service.UserMypageService;
import com.spring.client.domain.Member;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/memberManage")
@RequiredArgsConstructor
public class MemberManagementController {

	@Autowired
	private final MemberManagementService memberManagementService;

    private final UserMypageService userMypageService;
	/**
	 * 검색 기능 및 페이징 처리 제외
	 * @param member
	 * @return 
	 *
    @GetMapping
    public String memberList(Member member, Model model) {
    	List<Member> memberList = memberManagementService.memberList(member);
    	model.addAttribute("memberList", memberList);
    	
        return "admin/memberManage/memberList"; 
    } */
	
    @GetMapping
    public String memberList(
            @RequestParam(value = "state", defaultValue = "all") String state,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        // PageRequestDTO를 생성하고 필요한 값을 설정합니다.
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
            .page(page)
            .size(size)
            .build();

        // 필터 상태를 기반으로 데이터를 가져옵니다.
        PageResponseDTO<Member> memberList = memberManagementService.list(state, pageRequestDTO);
        model.addAttribute("memberList", memberList);
        model.addAttribute("currentStatus", state);  // 현재 필터 상태를 모델에 추가합니다.

        return "admin/memberManage/memberList";
    }

    
    /* 고객별 상세 페이지 */
    @GetMapping("/{memberNo}")
    public String memberDetail(@PathVariable Long memberNo, Model model) {
        // memberNo를 사용하여 Member 객체를 조회
        Member detail = memberManagementService.getMemberByMemberNo(memberNo);
        
        // Member 객체가 존재하는 경우
        if (detail != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            // 생성일 포맷팅
            String formattedDate_create = detail.getMemberCreatedAt().format(formatter);
            model.addAttribute("formattedDate_create", formattedDate_create);
            
            // 업데이트일 포맷팅
            String formattedDate_update = detail.getMemberUpdateAt() != null ? 
                detail.getMemberUpdateAt().format(formatter) : "";
            model.addAttribute("formattedDate_update", formattedDate_update);
            
            // 모델에 추가 정보 설정
            model.addAttribute("state", true);
            model.addAttribute("member", detail);
            
        } else {
            model.addAttribute("state", false);
            model.addAttribute("message", "회원정보를 찾을 수 없습니다.");
        }
        
        // 결과에 따라 동일한 템플릿을 반환
        return "admin/memberManage/memberDetail";
    }

    @PostMapping("/deleteMember")
    public ResponseEntity<String> deleteMember(@RequestParam("memberId") String memberId) {
        if (memberId != null) {
            userMypageService.nullifyUserData(memberId);  // 사용자 데이터 삭제
            return ResponseEntity.ok("DELETED");  // 성공 시 메시지 반환
        } else {
            return ResponseEntity.status(401).body("ERROR");  // 오류 시 메시지 반환
        }
    }

}
