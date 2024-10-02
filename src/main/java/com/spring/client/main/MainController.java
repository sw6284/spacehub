package com.spring.client.main;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spring.admin.space.domain.Space;
import com.spring.admin.space.service.SpaceService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	

	private final SpaceService spaceService;

	// 매핑 - 메인 페이지
	@GetMapping("/")
	public String mainClient() {
		return "client/main";
	}
	
	// 매핑 - 공간 리스트
	@GetMapping("/space")
	public String spaceList(Model model) {
		List<Space> spaceList= spaceService.spaceList(new Space());
		model.addAttribute("spaceList", spaceList);
		
		return "client/space/spaceList";
	}

    // 메인 페이지에 조회수 상위 공간 리스트 전달
    @GetMapping("/main")
    public String mainPage(Model model) {
        List<Space> topSpaces = spaceService.getTopSpaces(); // 조회수 상위 공간 리스트 가져오기
        model.addAttribute("topSpaces", topSpaces); // 모델에 추가하여 뷰로 전달
        return "client/main"; // 메인 페이지로 이동
    }
}
