
package com.spring.client.Inquiry.service;

import java.util.Collections;
import java.util.List;
//import java.util.stream.Collector;


import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.client.Inquiry.domain.Inquiry;
import com.spring.client.Inquiry.repository.InquiryRepository;
//import com.spring.common.vo.PageRequestDTO;
//import com.spring.common.vo.PageResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

	 @Autowired
	 private final InquiryRepository inquiryRepository;
	 
	 @Override
	    public void saveInquiry(Inquiry inquiry) {
	        // 로깅 추가
	        System.out.println("Saving Inquiry: " + inquiry.toString());
	        inquiry.setInqState("답변대기");
	        // Inquiry 엔티티를 직접 저장
	        inquiryRepository.save(inquiry);
	    }

	    
	    @Override
	    public List<Inquiry> inquiryList() {
	        return inquiryRepository.findByOrderByInqDateDesc();
	    }

	    @Override
	    public Inquiry inquiryDetail(Long inqNo) {
	        return inquiryRepository.findById(inqNo)
	                .orElseThrow(() -> new RuntimeException("Inquiry not found with id: " + inqNo));
	    }

	    @Override
	    public Inquiry getInquiry(Long no) {
	        return inquiryRepository.findById(no)
	                .orElseThrow(() -> new RuntimeException("Inquiry not found with id: " + no));
	    }

	    @Override
	    public boolean inquiryDelete(Long inqNo) {
	        try {
	            // 해당 문의글이 존재하는지 확인
	            if (inquiryRepository.existsById(inqNo)) {
	                // 문의글 삭제
	                inquiryRepository.deleteById(inqNo);
	                return true; // 삭제 성공
	            } else {
	                return false; // 문의글이 존재하지 않음
	            }
	        } catch (Exception e) {
	            // 예외 발생 시 로그를 남기거나 적절한 처리를 할 수 있습니다.
	            e.printStackTrace();
	            return false; // 삭제 실패
	        }
	    }

	    @Override
	    public void inquiryUpdate(Inquiry inquiry) {
	        Inquiry updateInquiry = inquiryRepository.findById(inquiry.getInqNo())
	                .orElseThrow(() -> new RuntimeException("Inquiry not found with id: " + inquiry.getInqNo()));

	        updateInquiry.setInqTitle(inquiry.getInqTitle());
	        updateInquiry.setInqContent(inquiry.getInqContent());

	        if (inquiry.getInqPassword() != null && !inquiry.getInqPassword().isEmpty()) {
	            updateInquiry.setInqPassword(inquiry.getInqPassword());
	        }

	        inquiryRepository.save(updateInquiry);
	    }

	    @Override
	    public void inquiryStateUpdate(Inquiry inquiry) {
	        Inquiry updateInquiry = inquiryRepository.findById(inquiry.getInqNo())
	                .orElseThrow(() -> new RuntimeException("Inquiry not found with id: " + inquiry.getInqNo()));
	        updateInquiry.setInqState(inquiry.getInqState());
	        inquiryRepository.save(updateInquiry);
	    }
/*
	    @Override
	    public PageResponseDTO<Inquiry> list(PageRequestDTO pageRequestDTO) {
	        Pageable pageable = PageRequest.of(
	                pageRequestDTO.getPage() - 1,
	                pageRequestDTO.getSize(), Sort.by("inqNo").descending());

	        Page<Inquiry> result = inquiryRepository.findAll(pageable);
	        List<Inquiry> inquiryList = result.getContent().stream().collect(Collectors.toList());
	        long totalCount = result.getTotalElements();
	        PageResponseDTO<Inquiry> reponseDTO = PageResponseDTO.<Inquiry>withAll().dtoList(inquiryList)
	        		.pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();
			
	        return reponseDTO;

	    }
*/

}

