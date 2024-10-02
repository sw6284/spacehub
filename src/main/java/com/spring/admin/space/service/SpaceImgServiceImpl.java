package com.spring.admin.space.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.admin.space.domain.SpaceImg;
import com.spring.admin.space.repository.SpaceImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceImgServiceImpl implements SpaceImgService{

	@Autowired
	public final SpaceImgRepository spaceImgRepository;

//	@Override
//	public List<SpaceImg> spaceImgList(SpaceImg spaceImg) {
//		List<SpaceImg> spaceList = null;
//		spaceList = (List<SpaceImg>) spaceImgRepository.findAll();
//		return spaceList;
//	}

	@Override
	public void spaceImgSave(SpaceImg spaceImg) {
		spaceImgRepository.save(spaceImg);		
	}

	@Override
	public SpaceImg spaceImgContent(SpaceImg spaceImg) {
		Optional<SpaceImg> spaceImgOptional = spaceImgRepository.findById(spaceImg.getApiNo());
		SpaceImg content = spaceImgOptional.get();
		return content;
	}
	
	@Override
	public SpaceImg getSpaceImg(Long apiNo) {
		Optional<SpaceImg> spaceImgOptional = spaceImgRepository.findById(apiNo);
		SpaceImg updateData = spaceImgOptional.get();
		return updateData;
	}
	
	@Override
	public void spaceImgUpdate(SpaceImg spaceImg) {
		Optional<SpaceImg> spaceImgOptional = spaceImgRepository.findById(spaceImg.getApiNo());
//		SpaceImg updateSpaceImg = spaceImgOptional.get();	
//		updateSpaceImg.setSpImg(spaceImg.getSpImg());
//		spaceImgRepository.save(updateSpaceImg);
		
		if (spaceImgOptional.isPresent()) {
            SpaceImg updateSpaceImg = spaceImgOptional.get();
            updateSpaceImg.setSpImg(spaceImg.getSpImg());
            spaceImgRepository.save(updateSpaceImg);
        }

	}

	@Override
	public void spaceImgDelete(SpaceImg spaceImg) {
		spaceImgRepository.deleteById(spaceImg.getApiNo());
	}

	@Override
	public SpaceImg getSpaceImgsBySpaceId(Long spNo) {
		return spaceImgRepository.findBySpaceDetailSpaceSpNo(spNo);
	}

}
