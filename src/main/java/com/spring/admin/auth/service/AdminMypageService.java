package com.spring.admin.auth.service;

import com.spring.admin.domain.Admin;

public interface AdminMypageService {
	Admin getAdminById(String admId);
    boolean checkPassword(String admId, String rawPassword);
	boolean updateAdmin(Admin updatedAdmin);
}
