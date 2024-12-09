package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ShareRepository extends JpaRepository<Share, Integer> {
}
