package com.spring.client.Inquiry.service;

import java.util.List;
import com.spring.client.Inquiry.domain.Inquiry;
import com.spring.client.Inquiry.repository.InquiryRepository;
//import com.spring.common.vo.PageRequestDTO;
//import com.spring.common.vo.PageResponseDTO;


public interface InquiryService {
    Inquiry inquiryDetail(Long inqNo);
    void saveInquiry(Inquiry inquiry);
    Inquiry getInquiry(Long no);
    boolean inquiryDelete(Long inqNo);
    void inquiryUpdate(Inquiry inquiry);
//	public PageResponseDTO<Inquiry> list(PageRequestDTO pageRequestDTO);
	public void inquiryStateUpdate(Inquiry inquiry);
	List<Inquiry> inquiryList();

}



