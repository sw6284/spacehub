package com.spring.admin.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.admin.domain.Admin;

public interface AdminMypageRepository extends JpaRepository<Admin, Long>{

	Optional<Admin> findByAdmId(String admId);
	    
    @Modifying
    @Query("UPDATE Admin a SET a.admName = ?2, a.admPhone = ?3, a.admPasswd = ?4 WHERE a.admId = ?1")
    int updateAdmin(String admId, String admName, String admPhone, String admPasswd);
}
