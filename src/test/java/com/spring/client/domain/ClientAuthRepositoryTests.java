package com.spring.client.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.client.auth.repository.UserAuthRepository;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ClientAuthRepositoryTests {
	@Setter(onMethod_ = @Autowired)
	private UserAuthRepository userAuthRepository;
	/* 회원가입 test 
	@Test
	public void userSignupTest() {
		Member member = new Member();
		member.setMemberId("gildong22");
		member.setMemberName("홍길동");
		member.setMemberEmail("gildong22@naver.com");
		member.setMemberPassword("gildong@22");
		member.setMemberPhone("010-1111-1111");
		log.info("### member 테이블에 두번째 데이터 입력");
		userAuthRepository.save(member);
	}*/
}
