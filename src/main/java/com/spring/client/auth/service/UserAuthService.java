package com.spring.client.auth.service;

import com.spring.client.domain.Member;

public interface UserAuthService {
    void saveMember(Member member);
    boolean isIdAvailable(String memberId);
	boolean isEmailAvailable(String memberEmail);
    Member userLogin(String memberId, String memberPassword);
    void sendVerificationCode(String email);
    boolean verifyCode(String email, String code);
    String findIdByNameAndEmail(String name, String email);
    boolean resetPassword(String memberId, String email);
	Member getMemberById(String memberId);
}
