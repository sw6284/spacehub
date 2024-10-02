package com.spring.admin.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.admin.domain.Admin;

public interface AdminAuthRepository extends JpaRepository<Admin, Long> {
	Admin findByAdmId(String admId);

	@Modifying
	@Query("UPDATE Admin a SET a.admName= NULL, a.admId=NULL, a.admPhone = NULL, a.admPasswd = NULL, a.admState = 'delete', a.admUpdateAt = CURRENT_TIMESTAMP WHERE a.admId = :admId")
	void nullifyAdminData(String admId);
}