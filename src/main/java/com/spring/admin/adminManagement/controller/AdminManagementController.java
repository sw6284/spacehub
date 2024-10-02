package com.spring.admin.adminManagement.controller;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.admin.adminManagement.service.AdminManagementService;
import com.spring.admin.domain.Admin;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/adminManage")
@RequiredArgsConstructor
public class AdminManagementController {

	@Autowired
	private final AdminManagementService adminManagementService;
	
	/**
	 * 검색 기능 및 페이징 처리 제외
	 * @param admin
	 * @return 
	 *
    @GetMapping
    public String adminList(Admin admin, Model model) {
    	List<Admin> adminList = adminManagementService.adminList(admin);
    	model.addAttribute("adminList", adminList);
    	
        return "admin/adminManage/adminList"; 
    }    
    */
	@GetMapping
	public String adminList(
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
	    PageResponseDTO<Admin> adminList = adminManagementService.list(state, pageRequestDTO);
	    model.addAttribute("adminList", adminList);
	    model.addAttribute("currentStatus", state);  // 현재 필터 상태를 모델에 추가합니다.

	    return "admin/adminManage/adminList";
	}

    /* 관리자별 상세 페이지 */
    @GetMapping("/{admNo}")
    public String memberDetail(@PathVariable Long admNo, Model model) {
        // memberNo를 사용하여 Member 객체를 조회
    	Admin detail = adminManagementService.getAdminByAdmNo(admNo);
        
        // Member 객체가 존재하는 경우
        if (detail != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            // 생성일 포맷팅
            String formattedDate_create = detail.getAdmCreatedAt().format(formatter);
            model.addAttribute("formattedDate_create", formattedDate_create);
            
            // 업데이트일 포맷팅
            String formattedDate_update = detail.getAdmUpdateAt() != null ? 
                detail.getAdmUpdateAt().format(formatter) : "";
            model.addAttribute("formattedDate_update", formattedDate_update);
            
            // 모델에 추가 정보 설정
            model.addAttribute("state", true);
            model.addAttribute("admin", detail);
            
        } else {
            model.addAttribute("state", false);
            model.addAttribute("message", "관리자정보를 찾을 수 없습니다.");
        }
        
        // 결과에 따라 동일한 템플릿을 반환
        return "admin/adminManage/adminDetail";
    }
    
    @GetMapping("/insertAdminForm")
    public String insertAdminForm(Admin admin) {
    	return "admin/adminManage/insertAdminForm";
    }
    
    @PostMapping("/insertAdmin")
    public String insertAdmin(Admin admin) {
    	adminManagementService.saveAdmin(admin);
        return "redirect:/admin/adminManage";
    }

}
