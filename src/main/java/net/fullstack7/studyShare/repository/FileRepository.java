package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
    File findByPostId(Integer id);
}
