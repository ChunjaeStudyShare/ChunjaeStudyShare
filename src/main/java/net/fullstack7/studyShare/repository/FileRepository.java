package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.File;
import net.fullstack7.studyShare.domain.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {
    File findByPostId(Integer id);
    //파일 id로 파일 삭제
    void deleteById(Integer id);

    void deleteByPostIn(List<Post> posts);
}


