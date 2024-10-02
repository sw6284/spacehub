package com.spring.client.Inquiry.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spring.client.Inquiry.domain.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

        // 특정 멤버가 작성한 문의 조회
        @Query("SELECT i FROM Inquiry i WHERE i.member.memberNo = ?1")
        List<Inquiry> findByMember(Long memberNo);

        // 특정 문의 번호와 비밀번호로 문의 조회
        Inquiry findByInqNoAndInqPassword(Long inqNo, String inqPassword);

        // 제목으로 문의 조회
        Inquiry findByInqTitle(String inqTitle);
        
        // 제목에 특정 단어가 포함된 문의 목록 조회
        List<Inquiry> findByInqTitleContaining(String inqTitle);
        
        // 작성자 이름에 특정 단어가 포함된 문의 목록 조회
        List<Inquiry> findByMember_MemberNameContaining(String memberName);

        // 내용에 특정 단어가 포함된 문의 목록 조회
        List<Inquiry> findByInqContentContaining(String inqContent);

        // 특정 날짜 범위 내의 문의 목록 조회
        List<Inquiry> findByInqDateBetween(LocalDateTime startDate, LocalDateTime endDate);
        
        // 번호를 기준으로 내림차순으로 정렬된 문의 목록 조회
        List<Inquiry> findByOrderByInqDateDesc();
        

    }

