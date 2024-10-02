package com.spring.client.auth.service;

import com.spring.client.domain.Member;

public interface UserMypageService {
	//public List<Inquiry> inquiryList(Inquiry inquiry);
	//public PageResponseDTO<Inquiry> list(PageRequestDTO pageRequestDTO);
    Member getMemberById(String memberId);
    boolean updateMember(Member updatedmember);
    boolean checkPassword(String memberId, String password);
	void nullifyUserData(String memberId);
}