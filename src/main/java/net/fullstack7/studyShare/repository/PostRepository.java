package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
