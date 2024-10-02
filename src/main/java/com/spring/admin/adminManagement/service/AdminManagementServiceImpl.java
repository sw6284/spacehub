package com.spring.admin.adminManagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.spring.admin.adminManagement.repository.AdminManagementRepository;
import com.spring.admin.domain.Admin;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminManagementServiceImpl implements AdminManagementService {
	private final AdminManagementRepository adminManagementRepository;
    
    @Override
    public List<Admin> adminList(Admin admin) {
        return adminManagementRepository.findAllByOrderByAdmNoAsc(); // 전체 목록 반환
    }
    @Override
    public PageResponseDTO<Admin> list(String state, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
            pageRequestDTO.getPage() - 1, // 1페이지가 0이므로 주의
            pageRequestDTO.getSize(), Sort.by("admNo").descending()
        );

        // 필터 조건을 추가하는 부분 (필터 조건에 따라 쿼리 수정 필요)
        Page<Admin> result;
        if ("all".equals(state)) {
            result = adminManagementRepository.findAll(pageable);
        } else {
            result = adminManagementRepository.findByAdmState(state, pageable);
        }
        
        List<Admin> adminList = result.getContent().stream().collect(Collectors.toList());
        long totalCount = result.getTotalElements();

        PageResponseDTO<Admin> responseDTO = PageResponseDTO.<Admin>withAll()
            .dtoList(adminList)
            .pageRequestDTO(pageRequestDTO)
            .totalCount(totalCount)
            .build();
            
        return responseDTO;
    }


    
    @Override
    public Admin getAdminByAdmNo(Long admNo){
        return adminManagementRepository.findByAdmNo(admNo); // memberNo로 조회
    }
    
    @Override
    public void saveAdmin(Admin admin) {
    	// TODO Auto-generated method stub
    	adminManagementRepository.save(admin);
    }
}
