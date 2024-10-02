package com.spring.admin.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.admin.auth.repository.AdminAuthRepository;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class AdminAuthRepositoryTest {

	@Setter(onMethod_ = @Autowired)
	private AdminAuthRepository adminAuthRepository;
	
	@Test
	public void userSignupTest() {
		Admin admin = new Admin();
		admin.setAdmId("superAdmin");
		admin.setAdmName("박주영");
		admin.setAdmEmail("superAdmin@spacehub.com");
		admin.setAdmPasswd("superAdmin@22");
		admin.setAdmPhone("010-1111-1111");
		admin.setAdmSuper("Y");
		adminAuthRepository.save(admin);
	}
}
