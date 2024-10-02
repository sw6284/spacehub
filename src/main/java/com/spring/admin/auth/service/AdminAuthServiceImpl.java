package com.spring.admin.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.admin.auth.repository.AdminAuthRepository;
import com.spring.admin.domain.Admin;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {
	
	@Autowired
    private AdminAuthRepository adminAuthRepository;
	
	@Override
	public Admin adminLogin(String admId, String admPassword) {
		System.out.println("입력받은 Id: " + admId);
		System.out.println("입력받은 pwd: " + admPassword);
	    Admin admin = adminAuthRepository.findByAdmId(admId);
	    
	    // Admin 객체의 정보 출력
	    if (admin != null) {
	        System.out.println("Admin found: " + admin);
	        System.out.println("Admin ID: " + admin.getAdmId());
	        System.out.println("Admin Password: " + admin.getAdmPasswd());
	    } else {
	        System.out.println("No admin found with ID: " + admId);
	    }

	    if (admin != null && admin.getAdmPasswd().equals(admPassword)) {
	        return admin;
	    } else {
	        return null;
	    }
	}

	
	@Override
	public Admin getAdminById(String admId) {
		return adminAuthRepository.findByAdmId(admId);
	}

    @Transactional
    @Override
	public void nullifyAdminData(String admId) {
    	adminAuthRepository.nullifyAdminData(admId);
	}
}
