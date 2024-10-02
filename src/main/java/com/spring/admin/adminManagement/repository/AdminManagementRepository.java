package com.spring.admin.adminManagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.admin.domain.Admin;

public interface AdminManagementRepository extends JpaRepository<Admin, Long>{
    List<Admin> findAllByOrderByAdmNoAsc(); // admNo를 기준으로 오름차순 정렬

    Admin findByAdmNo(Long admNo); // admNo로 Admin 조회

	Page<Admin> findByAdmState(String admState, Pageable pageable);
}
