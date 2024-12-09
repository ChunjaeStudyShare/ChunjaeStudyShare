package net.fullstack7.studyShare.service.admin;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Admin;
import net.fullstack7.studyShare.repository.AdminRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class AdminService {

    private final AdminRepository adminRepository;

    public boolean login(String adminId, String password) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("관리자 아이디가 존재하지 않습니다."));
        if(!admin.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return true;
    }
}
