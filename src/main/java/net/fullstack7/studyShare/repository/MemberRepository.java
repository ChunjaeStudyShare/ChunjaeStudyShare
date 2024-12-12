package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserId(String userId);
    Page<Member> findAll(Pageable pageable);
    Page<Member> findByUserIdContaining(String userId, Pageable pageable);
    Page<Member> findByNameContaining(String name, Pageable pageable);
    Page<Member> findByEmailContaining(String email, Pageable pageable);
    Page<Member> findByStatus(Integer status, Pageable pageable);
    Page<Member> findByUserIdContainingOrNameContainingOrEmailContaining(
        String userId, String name, String email, Pageable pageable);
    void deleteById(String userId);
}
