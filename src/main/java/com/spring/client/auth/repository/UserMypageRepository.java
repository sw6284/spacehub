package com.spring.client.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.spring.client.domain.Member;

public interface UserMypageRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.memberName = ?2, m.memberPhone = ?3, m.memberPassword = ?4, m.memberEmail = ?5 WHERE m.memberId = ?1")
    int updateMember(String memberId, String memberName, String memberPhone, String memberPassword, String memberEmail);

    
    @Modifying
    @Query("UPDATE Member m SET m.memberId = NULL, m.memberName = '탈퇴회원', m.memberPhone = NULL, m.memberPassword = NULL, m.memberState = 'delete', m.kakaoId = NULL, m.kakaoConnectAt = NULL, m.memberUpdateAt = CURRENT_TIMESTAMP WHERE m.memberId = :memberId")
    void nullifyUserData(@Param("memberId") String memberId);

}



