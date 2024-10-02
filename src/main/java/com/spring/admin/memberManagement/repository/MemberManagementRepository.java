package com.spring.admin.memberManagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.client.domain.Member;

public interface MemberManagementRepository extends JpaRepository<Member, Long>{
    List<Member> findAllByOrderByMemberNoAsc(); // memberNo를 기준으로 오름차순 정렬

    Member findByMemberNo(Long memberNo); // memberNo로 Member 조회
    Page<Member> findByMemberState(String memberState, Pageable pageable);
}
