package com.spring.admin.inquiry.service;

import java.util.List;

import com.spring.admin.inquiry.domain.InquiryAnswer;
import com.spring.client.Inquiry.domain.Inquiry;

public interface AnswerService {
	public InquiryAnswer inquiryanswerList(InquiryAnswer inquiryanswer);
	public InquiryAnswer inquiryanswerInsert(InquiryAnswer inquiryanswer);
	public InquiryAnswer inquiryanswerUpdate(InquiryAnswer inquiryanswer);
	public void inquiryanswerDelete(InquiryAnswer inquiryanswer);

}
