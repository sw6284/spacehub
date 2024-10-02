package com.spring.client.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.client.auth.repository.UserMypageRepository;
import com.spring.client.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMypageServiceImpl implements UserMypageService {
	private final UserMypageRepository userMypageRepository;
//	private final InquiryRepository inquiryRepository;
    @Override
    public Member getMemberById(String memberId) {
        return userMypageRepository.findByMemberId(memberId)
                .orElse(null); // 관리자 정보를 찾지 못하면 null 반환
    }

    @Override
    public boolean checkPassword(String memberId, String rawPassword) {
    	Member member = userMypageRepository.findByMemberId(memberId).orElse(null);
        return member != null && rawPassword.equals(member.getMemberPassword()); // 평문 비밀번호와 비교
    }

    @Transactional
    @Override
    public boolean updateMember(Member updatedMember) {
        if (userMypageRepository.findByMemberId(updatedMember.getMemberId()).isPresent()) {
            int result = userMypageRepository.updateMember(
        		updatedMember.getMemberId(),
        		updatedMember.getMemberName(),
        		updatedMember.getMemberPhone(),
        		updatedMember.getMemberPassword(),
        		updatedMember.getMemberEmail()
            );

            return result > 0; // 업데이트가 성공적으로 수행되었는지 확인
        }
        return false;
    }
    @Transactional
    @Override
    public void nullifyUserData(String memberId) {
    	userMypageRepository.nullifyUserData(memberId);
    }
       
    /*
    @Override
    public List<Inquiry> inquiryList(Inquiry inquiry) {
        return (List<Inquiry>) inquiryRepository.findAll();
    }

    @Override
    public PageResponseDTO<Inquiry> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
            pageRequestDTO.getPage() - 1, // 1페이지가 0이므로 주의
            pageRequestDTO.getSize(),
            Sort.by("inquiryNo").descending() // 실제 도메인에 맞는 정렬 기준
        );

        Page<Inquiry> result = inquiryRepository.findAll(pageable);
        List<Inquiry> inquiryList = result.getContent();
        long totalCount = result.getTotalElements();

        PageResponseDTO<Inquiry> responseDTO = PageResponseDTO.<Inquiry>withAll()
            .dtoList(inquiryList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(totalCount)
            .build();

        return responseDTO;
    }
     */
}
