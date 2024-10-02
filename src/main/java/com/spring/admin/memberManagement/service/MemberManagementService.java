package com.spring.admin.memberManagement.service;

import java.util.List;

import com.spring.client.domain.Member;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

public interface MemberManagementService {

	List<Member> memberList(Member member);
	Member getMemberByMemberNo(Long memberNo);
	PageResponseDTO<Member> list(String state, PageRequestDTO pageRequestDTO);

}
