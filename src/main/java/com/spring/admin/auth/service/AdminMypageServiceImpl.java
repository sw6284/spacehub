package com.spring.admin.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.admin.auth.repository.AdminMypageRepository;
import com.spring.admin.domain.Admin;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminMypageServiceImpl implements AdminMypageService {
    private final AdminMypageRepository adminMypageRepository;

    @Override
    public Admin getAdminById(String admId) {
        return adminMypageRepository.findByAdmId(admId)
                .orElse(null); // 관리자 정보를 찾지 못하면 null 반환
    }

    @Override
    public boolean checkPassword(String admId, String rawPassword) {
        Admin admin = adminMypageRepository.findByAdmId(admId).orElse(null);
        return admin != null && rawPassword.equals(admin.getAdmPasswd()); // 평문 비밀번호와 비교
    }

    @Transactional
    @Override
    public boolean updateAdmin(Admin updatedAdmin) {
        if (adminMypageRepository.findByAdmId(updatedAdmin.getAdmId()).isPresent()) {
            int result = adminMypageRepository.updateAdmin(
                updatedAdmin.getAdmId(),
                updatedAdmin.getAdmName(),
                updatedAdmin.getAdmPhone(),
                updatedAdmin.getAdmPasswd()
            );

            return result > 0; // 업데이트가 성공적으로 수행되었는지 확인
        }
        return false;
    }
}
