package com.spring.admin.space.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.admin.space.domain.Space;
import com.spring.admin.space.domain.SpaceDetail;
import com.spring.admin.space.domain.SpaceImg;
import com.spring.admin.space.service.SpaceDetailService;
import com.spring.admin.space.service.SpaceImgService;
import com.spring.admin.space.service.SpaceService;
import com.spring.common.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/space")
@RequiredArgsConstructor
public class AdminSpaceController {

    private final SpaceService spaceService;

	private final SpaceDetailService spaceDetailService;
	
	private final SpaceImgService spaceImgService;
	
	private final CustomFileUtil fileUtil;
	
    // 공간 리스트 조회
    @GetMapping("/spaceList")
    public String spaceList(Model model, Space space) {
        List<Space> spaceList = spaceService.spaceList(space);
//        SpaceImg spaceImgs = spaceImgService.getSpaceImgsBySpaceId(space.getSpNo());
        
        model.addAttribute("spaceList", spaceList);
//        model.addAttribute("spaceImgs", spaceImgs);
        
        return "admin/space/adminSpaceList";
    }
    
//    @ResponseBody
//    @GetMapping("/spaceList1")
//    public  List<Space> spaceList1(Space space) {
//        List<Space> spaceList = spaceService.spaceList(space);
//       
//        return spaceList;
//    }

    // 공간 상세 조회
    @GetMapping("/{spNo}")
    public String spaceDetail(@PathVariable Long spNo, Model model) {
        Space space = spaceService.getSpaceById(spNo);
        SpaceDetail spaceDetail = spaceDetailService.getSpaceDetailBySpaceId(spNo);
        SpaceImg spaceImgs = spaceImgService.getSpaceImgsBySpaceId(spNo);
        
        model.addAttribute("space", space);
        model.addAttribute("spaceDetail", spaceDetail);
        model.addAttribute("spaceImgs", spaceImgs);
        
        return "admin/space/adminSpaceDetail";
    }
    
    // 공간 등록 폼
    @GetMapping("/insertForm")
    public String insertForm(Model model) {
        model.addAttribute("space", new Space());
        model.addAttribute("spaceDetail", new SpaceDetail());
        model.addAttribute("spaceImg", new SpaceImg());
        
        return "admin/space/adminInsertForm";
    }

    @PostMapping("/spaceInsert")
    public String spaceSave(Space space, SpaceDetail spaceDetail, @RequestParam List<MultipartFile> spImgFiles) {
    //public String spaceSave(Space space, SpaceDetail spaceDetail, SpaceImg spaceImg) {
        // admNo가 null이 아닌지 확인
        if (space.getAdmNo() == null) {
            // 오류 처리 로직 추가 (예: 예외를 던지거나 사용자에게 오류 메시지 반환)
            throw new IllegalArgumentException("admNo must not be null");
        }

        // 공간 정보 저장
        spaceService.spaceSave(space);

        // 공간 상세 정보 저장
        spaceDetail.setSpace(space);
        spaceDetailService.spaceDetailSave(spaceDetail);

        // 이미지 저장
        for (MultipartFile file : spImgFiles) {
            SpaceImg spaceImg = new SpaceImg();
            String fileName = fileUtil.saveFile(file, "spaceImg"); // 파일 저장 로직 추가 필요
            spaceImg.setSpImg(fileName);
            spaceImg.setSpaceDetail(spaceDetail); // 공간 상세 정보와 연결
            spaceImgService.spaceImgSave(spaceImg);
        }
        
        /*if(!spaceImg.getFile().isEmpty()) {
			String uploadFileName = fileUtil.saveFile(spaceImg.getFile(), "spaceImg");
			spaceImg.setSpImg(uploadFileName);
			spaceImg.setSpaceDetail(spaceDetail); // 공간 상세 정보와 연결
	        spaceImgService.spaceImgSave(spaceImg);
		}*/

        return "redirect:/admin/space/spaceList";
    }

    
    /*private String saveImgFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("/SpaceHub/attachment/spaceImg/" + fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }*/


	/*// 공간 수정 폼
    @GetMapping("/updateForm/{spNo}")
    public String updateForm(@PathVariable Long spNo, Long spDetail, Long apiNo, Model model) {
        Space space = spaceService.getSpace(spNo);
        SpaceDetail spaceDetail = spaceDetailService.getSpaceDetail(spDetail);
        SpaceImg spaceImg = spaceImgService.getSpaceImg(apiNo);
        
        model.addAttribute("updateData", space);
        model.addAttribute("updateDate", spaceDetail);
        model.addAttribute("updateDate", spaceImg);
        return "admin/space/updateForm";
    }*/
 // 공간 수정 폼
    @GetMapping("/updateForm/{spNo}")
    public String updateForm(
            @PathVariable Long spNo, 
            @RequestParam(required = false) Long spDetail, 
            @RequestParam(required = false) Long apiNo, Model model) {

        Space space = spaceService.getSpace(spNo);

        // Null 검사를 추가하여 예외를 방지
        SpaceDetail spaceDetail = (spDetail != null) ? spaceDetailService.getSpaceDetail(spDetail) : new SpaceDetail();
        SpaceImg spaceImg = (apiNo != null) ? spaceImgService.getSpaceImg(apiNo) : new SpaceImg();

        model.addAttribute("updateSpace", space);
        model.addAttribute("updateSpaceDetail", spaceDetail);
        model.addAttribute("updateSpaceImg", spaceImg);
        
        return "admin/space/adminUpdateForm";
    }


    /*// 공간 수정 처리
    @PostMapping("/spaceUpdate")
    public String spaceUpdate(Space space) {
        spaceService.spaceUpdate(space);
        return "redirect:/admin/space/" + space.getSpNo();
    }*/
    
    @PostMapping("/spaceUpdate")
    public String spaceUpdate(
            @RequestParam Long spNo,
            @RequestParam String spName,
            @RequestParam Long spHourPrice,
            @RequestParam String spKeyword,
            @RequestParam(required = false) MultipartFile spaceImg, Model model) {

        // 공간 정보 업데이트
        Space space = spaceService.getSpace(spNo);
        space.setSpName(spName);
        space.setSpHourPrice(spHourPrice);
        space.setSpKeyword(spKeyword);
        spaceService.spaceUpdate(space);
        
        //공간 상세 정보 가져오기
        SpaceDetail spaceDetail = spaceDetailService.getSpaceDetail(spNo);
        // 기존 이미지 가져오기
        SpaceImg existingImage = spaceImgService.getSpaceImgsBySpaceId(spNo);
        // 기존 이미지 삭제
        if (existingImage != null) {
            fileUtil.deleteFile(existingImage.getSpImg());
            spaceImgService.spaceImgDelete(existingImage); // 이미지 삭제 메서드
        }

     // 새로운 이미지 저장
        if (spaceImg != null && !spaceImg.isEmpty()) {
            SpaceImg newImage = new SpaceImg();
            String fileName = fileUtil.saveFile(spaceImg, "spaceImg");
            newImage.setSpImg(fileName);
            newImage.setSpaceDetail(spaceDetail); // 공간 상세 정보와 연결
            spaceImgService.spaceImgSave(newImage);
        }
        
/*        // 새로운 이미지 저장
        if (spImgFiles != null) {
            for (MultipartFile file : spImgFiles) {
                if (!file.isEmpty()) {
                    SpaceImg spaceImg = new SpaceImg();
                    String fileName = fileUtil.saveFile(file, "spaceImg");
                    spaceImg.setSpImg(fileName);
                    spaceImg.setSpaceDetail(spaceDetail); // 공간 상세 정보와 연결
                    spaceImgService.spaceImgSave(spaceImg);
                }
            }
        }*/

        return "redirect:/admin/space/" + spNo;
    }


    // 공간 삭제 처리
    @PostMapping("/spaceDelete/{spNo}")
    public String spaceDelete(@PathVariable Long spNo) {
        SpaceDetail deleteSpaceDetailData = spaceDetailService.getSpaceDetailBySpNo(spNo);
        SpaceImg deleteSpaceImgData = spaceImgService.getSpaceImgsBySpaceId(spNo);
        
        spaceService.spaceDelete(spNo);
        
        if(deleteSpaceImgData.getSpImg()!=null) {
			fileUtil.deleteFile(deleteSpaceImgData.getSpImg());
		}
        return "redirect:/admin/space/spaceList";
    }
	
    /* 업로드 파일 보여주기 */
	@ResponseBody
	@GetMapping("/view/{spImg}")
	public ResponseEntity<Resource> viewFileGET(@PathVariable String spImg) {
		return fileUtil.getFile(spImg, "spaceImg");
	}
}
