package com.spring.client.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.client.domain.Member;

public interface UserAuthRepository extends JpaRepository<Member, Long> {

    // ID로 Member 존재 여부 확인
    boolean existsByMemberId(String memberId);

    // Email로 Member 존재 여부 확인
	boolean existsByMemberEmail(String memberEmail);
    // memberId로 Member 조회
    Member findByMemberId(String memberId);

    // 이름과 이메일로 Member 조회
    Member findByMemberNameAndMemberEmail(String memberName, String memberEmail);
    
    // 이름, memberId, 이메일로 Member 조회
    Member findByMemberNameAndMemberIdAndMemberEmail(String memberName, String memberId, String memberEmail);

}
