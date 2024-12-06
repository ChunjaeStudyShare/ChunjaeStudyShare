package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
