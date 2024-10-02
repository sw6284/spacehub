package com.spring.admin.auth.service;

import com.spring.admin.domain.Admin;

public interface AdminAuthService {
	public Admin adminLogin(String admId, String admPassword);
	public Admin getAdminById(String admId);
	public void nullifyAdminData(String admId);
}
