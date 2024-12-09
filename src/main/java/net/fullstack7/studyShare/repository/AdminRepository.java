package net.fullstack7.studyShare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.fullstack7.studyShare.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findById(String adminId);
}