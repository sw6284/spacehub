package com.spring.admin.space.service;

import com.spring.admin.space.domain.SpaceImg;

public interface SpaceImgService {

	//List<SpaceImg> spaceImgList(SpaceImg spaceImg);
	void spaceImgSave(SpaceImg spaceImg);
	SpaceImg spaceImgContent (SpaceImg spaceImg);
	void spaceImgUpdate (SpaceImg spaceImg);
	void spaceImgDelete (SpaceImg spaceImg);
	SpaceImg getSpaceImg(Long apiNo);
	SpaceImg getSpaceImgsBySpaceId(Long spNo);

}
