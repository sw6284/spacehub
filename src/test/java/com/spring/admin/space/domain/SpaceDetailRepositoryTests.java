package com.spring.admin.space.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.admin.space.repository.SpaceDetailRepository;
import com.spring.admin.space.repository.SpaceImgRepository;
import com.spring.admin.space.repository.SpaceRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class SpaceDetailRepositoryTests {
	
	@Autowired
	private SpaceDetailRepository spaceDetailRepository;
	
	@Autowired
    private SpaceRepository spaceRepository; // Space 엔티티 저장을 위해 추가
	
    @Autowired
    private SpaceImgRepository spaceImgRepository;
    
    
    /*기존 데이터 삭제 후 새로 추가 */
    @Test
    public void testDeleteAndRecreateSpaceDetailAndSpaceImg() {
        /*
    	// 1. 기존 데이터 삭제
        spaceImgRepository.deleteAll();      // SpaceImg 먼저 삭제
        spaceDetailRepository.deleteAll();   // 그 다음 SpaceDetail 삭제
        spaceRepository.deleteAll();         // 마지막으로 Space 삭제
        */
    	
    	/* 이거 아직 안 써봄. 근데 not null 때문에 안 될 거 같은데...
        // 2. 새로운 데이터 생성
        // Space 생성
        Space space = new Space();
        space.setSpName("새로운 테스트 공간");
        spaceRepository.save(space);

        // SpaceDetail 생성 및 저장
        SpaceDetail spaceDetail = new SpaceDetail();
        spaceDetail.setSpace(space);
        spaceDetail.setSpDescription("새로운 세부 정보");
        spaceDetailRepository.save(spaceDetail);

        // SpaceImg 생성 및 저장
        SpaceImg spaceImg = new SpaceImg();
        spaceImg.setSpaceDetail(spaceDetail);
        spaceImg.setSpImg("새 이미지 URL");
        spaceImgRepository.save(spaceImg);

        // 3. 데이터 검증
        assertNotNull(spaceDetail.getSpDetail());  // SpaceDetail 저장 확인
        assertNotNull(spaceImg.getApiNo());          // SpaceImg 저장 확인
        assertEquals(spaceImg.getSpaceDetail().getSpDetail(), spaceDetail.getSpDetail());  // 관계 확인
        */
    	
    }
    /*
    @Test
    public void testAddMultipleImagesToSpace() {
        // 1. Space 생성
        Space space = new Space();
        space.setAdmNo(1234L);
        space.setSpName("테스트 14");
        space.setSpCapacity("8~10");
        space.setSpHourPrice(10000L);
        space.setSpKeyword("회의실");

        spaceRepository.save(space);

        // 2. SpaceDetail 생성 및 저장
        SpaceDetail spaceDetail = new SpaceDetail();
        spaceDetail.setSpace(space);
        spaceDetail.setSpDescription("JUnit 테스트 데이터");
        spaceDetail.setSpEquipment("프로젝터, 화이트보드");
        spaceDetail.setSpRules("음식물 반입 금지");
        spaceDetail.setSpStartTime("09:00");
        spaceDetailRepository.save(spaceDetail);

        // 3. 여러 SpaceImg 생성 및 저장
        String[] imageUrls = {"space2-1.jpg", "space2-2.jpg", "space2-3.jpg"};

        for (String imageUrl : imageUrls) {
            SpaceImg spaceImg = new SpaceImg();
            spaceImg.setSpaceDetail(spaceDetail);  // SpaceDetail과 연결
            spaceImg.setSpImg(imageUrl);
            spaceImgRepository.save(spaceImg);
        }

        // 4. 데이터 검증
        List<SpaceImg> images = spaceImgRepository.findBySpaceDetail(spaceDetail);
        assertEquals(imageUrls.length, images.size());  // 저장된 이미지 개수 확인
        
        // 5. 파일 경로 검증
//        for (String imageUrl : imageUrls) {
//            Path path = Paths.get("src/test/resources" + imageUrl);
//            assertTrue(Files.exists(path));  // 파일이 실제로 존재하는지 확인
//        }
    }  */
	
}
