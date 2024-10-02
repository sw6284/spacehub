package com.spring.admin.adminManagement.service;

import java.util.List;

import com.spring.admin.domain.Admin;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

public interface AdminManagementService {

	List<Admin> adminList(Admin admin);
	Admin getAdminByAdmNo(Long admNo);
	void saveAdmin(Admin admin);
	PageResponseDTO<Admin> list(String state, PageRequestDTO pageRequestDTO);

}
