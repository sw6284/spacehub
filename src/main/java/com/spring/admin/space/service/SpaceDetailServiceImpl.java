package com.spring.admin.space.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.admin.space.domain.SpaceDetail;
import com.spring.admin.space.repository.SpaceDetailRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceDetailServiceImpl implements SpaceDetailService {

	@Autowired
	public final SpaceDetailRepository spaceDetailRepository;
	
	@Override
	public List<SpaceDetail> spaceDetailList(SpaceDetail spaceDetail) {
		List<SpaceDetail> spaceDetailList = spaceDetailRepository.findAll();
		return spaceDetailList;
	}

	@Override
	public void spaceDetailSave(SpaceDetail spaceDetail) {
		spaceDetailRepository.save(spaceDetail);
	}
	
	@Override	// 상세페이지
	public SpaceDetail spaceDetailContent(SpaceDetail spaceDetail) {
		Optional<SpaceDetail> spaceDetailOptional = spaceDetailRepository.findById(spaceDetail.getSpDetail());
		SpaceDetail content = spaceDetailOptional.get();
		return content;
	}

	@Override
	public SpaceDetail getSpaceDetail(Long spDetail) {
		Optional<SpaceDetail> spaceDetailOptional = spaceDetailRepository.findById(spDetail);
		SpaceDetail updateData = spaceDetailOptional.orElseThrow();
		return updateData;
	}
	@Override
	public SpaceDetail getSpaceDetailBySpNo(Long spNo) {
		Optional<SpaceDetail> spaceDetailOptional = spaceDetailRepository.findBySpaceSpNo(spNo);
		SpaceDetail updateData = spaceDetailOptional.orElseThrow();
		return updateData;
	}
	@Override
	public void spaceDetailUpdate(SpaceDetail spaceDetail) {
		Optional<SpaceDetail> spaceDetailOptional = spaceDetailRepository.findById(spaceDetail.getSpDetail());
		SpaceDetail updateSpaceDetail = spaceDetailOptional.get();
		updateSpaceDetail.setSpDescription(spaceDetail.getSpDescription());
		updateSpaceDetail.setSpEquipment(spaceDetail.getSpEquipment());
		updateSpaceDetail.setSpStartTime(spaceDetail.getSpStartTime());
		
		spaceDetailRepository.save(updateSpaceDetail);
		
	}

	@Override
	public void spaceDetailDelete(Long spNo) {
		spaceDetailRepository.deleteBySpNo(spNo);	
	}

	@Override
	 public SpaceDetail getSpaceDetailBySpaceId(Long spNo) {
        return spaceDetailRepository.findBySpaceSpNo(spNo)
            .orElseThrow(() -> new EntityNotFoundException("SpaceDetail not found for space id " + spNo));
    }



}


