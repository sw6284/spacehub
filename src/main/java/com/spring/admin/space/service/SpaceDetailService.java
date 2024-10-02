package com.spring.admin.space.service;

import java.util.List;

import com.spring.admin.space.domain.SpaceDetail;

public interface SpaceDetailService {
	List<SpaceDetail> spaceDetailList(SpaceDetail spaceDetail);
	void spaceDetailSave(SpaceDetail spaceDetail);
	SpaceDetail spaceDetailContent (SpaceDetail spaceDetail);
	void spaceDetailUpdate (SpaceDetail spaceDetail);
	void spaceDetailDelete (Long spNo);
	SpaceDetail getSpaceDetail(Long spDetail);
	SpaceDetail getSpaceDetailBySpaceId(Long spNo);
	public SpaceDetail getSpaceDetailBySpNo(Long spNo);
}
