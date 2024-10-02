package com.spring.admin.inquiry.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.admin.inquiry.domain.InquiryAnswer;
import com.spring.admin.inquiry.repository.InquiryAnswerRepository;
import com.spring.client.Inquiry.domain.Inquiry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final InquiryAnswerRepository inquiryAnswerRepository;

    
    @Override
    public InquiryAnswer inquiryanswerList(InquiryAnswer inquiryanswer) {
    	InquiryAnswer inquiryanswerList = inquiryAnswerRepository.findByInquiryNo(inquiryanswer.getInquiry().getInqNo());
    	return inquiryanswerList;
    }

	@Override
	public InquiryAnswer inquiryanswerInsert(InquiryAnswer inquiryanswer) {
		inquiryanswer.setAnsState("답변완료");
		InquiryAnswer result = inquiryAnswerRepository.save(inquiryanswer);
		return result;
	}

	@Override
	public InquiryAnswer inquiryanswerUpdate(InquiryAnswer inquiryanswer) {
		Optional<InquiryAnswer> inquiryAnswerOptional = inquiryAnswerRepository.findById(inquiryanswer.getAnsNo());
		InquiryAnswer inquiryanswerUpdate = inquiryAnswerOptional.get();
		inquiryanswerUpdate.setAnswer(inquiryanswer.getAnswer());
		InquiryAnswer result = inquiryAnswerRepository.save(inquiryanswerUpdate);
		return result;
	}

	@Override
	public void inquiryanswerDelete(InquiryAnswer inquiryanswer) {
		inquiryAnswerRepository.deleteById(inquiryanswer.getAnsNo());
		
	}

}
