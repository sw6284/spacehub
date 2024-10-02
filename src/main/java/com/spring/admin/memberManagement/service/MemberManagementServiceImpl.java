package com.spring.admin.memberManagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.spring.admin.memberManagement.repository.MemberManagementRepository;
import com.spring.client.domain.Member;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberManagementServiceImpl implements MemberManagementService {
    private final MemberManagementRepository memberManagementRepository;
    
    @Override
    public List<Member> memberList(Member member) {
        return memberManagementRepository.findAllByOrderByMemberNoAsc(); // 전체 목록 반환
    }
    
    @Override
    public Member getMemberByMemberNo(Long memberNo) {
        return memberManagementRepository.findByMemberNo(memberNo); // memberNo로 조회
    }
    
    public PageResponseDTO<Member> list(String state, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
            pageRequestDTO.getPage() - 1, // 1페이지가 0이므로 주의
            pageRequestDTO.getSize(), 
            Sort.by("memberNo").descending()
        );

        Page<Member> result;
        if ("all".equals(state)) {
            result = memberManagementRepository.findAll(pageable);
        } else {
            result = memberManagementRepository.findByMemberState(state, pageable);
        }
        
        List<Member> memberList = result.getContent().stream().collect(Collectors.toList());
        long totalCount = result.getTotalElements();
        return PageResponseDTO.<Member>withAll()
            .dtoList(memberList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(totalCount)
            .build();
    }

}
