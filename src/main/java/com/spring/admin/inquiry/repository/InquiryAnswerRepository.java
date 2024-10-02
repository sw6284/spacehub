package com.spring.admin.inquiry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.admin.inquiry.domain.InquiryAnswer;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
 
	@Query("SELECT c FROM InquiryAnswer c WHERE c.inquiry.inqNo = ?1")
	InquiryAnswer findByInquiryNo(Long no);
	InquiryAnswer findBymemberId(String memberId);
}
