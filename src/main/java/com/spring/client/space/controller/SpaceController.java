package com.spring.client.space.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.spring.admin.space.domain.Space;
import com.spring.admin.space.domain.SpaceDetail;
import com.spring.admin.space.domain.SpaceImg;
import com.spring.admin.space.service.SpaceDetailService;
import com.spring.admin.space.service.SpaceImgService;
import com.spring.admin.space.service.SpaceService;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequestMapping("/space")
@RequiredArgsConstructor
public class SpaceController {
	
	@Setter(onMethod_ = @Autowired)
	private SpaceService spaceService;

	@Setter(onMethod_ = @Autowired)
	private SpaceDetailService spaceDetailService;
	
	@Setter(onMethod_ = @Autowired)
	private SpaceImgService spaceImgService;
	
	// 공간 리스트 조회
	@GetMapping("/spaceList")
	public String spaceList(Space space, Model model) {
		List<Space> spaceList= spaceService.spaceList(space);
		model.addAttribute("spaceList", spaceList);
		
		return "client/space/spaceList";
	}
	
	@GetMapping("/{spNo}")
    public String spaceDetail(@PathVariable Long spNo, Model model) {
		spaceService.mountHitCount(spNo);
		
        Space space = spaceService.getSpaceById(spNo);
        SpaceDetail spaceDetail = spaceDetailService.getSpaceDetailBySpaceId(spNo);
        SpaceImg spaceImgs = spaceImgService.getSpaceImgsBySpaceId(spNo);
        
        model.addAttribute("space", space);
        model.addAttribute("spaceDetail", spaceDetail);
        model.addAttribute("spaceImgs", spaceImgs);
        
        return "client/space/spaceDetail";
    }
	
}
