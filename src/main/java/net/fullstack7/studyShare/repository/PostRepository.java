package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.dto.post.PostViewDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.time.LocalDateTime;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT p.*, f.fileName AS fileName, f.path AS path " +
            "FROM post p " +
            "LEFT JOIN file f ON p.id = f.postId " +
            "WHERE p.id = :postId", nativeQuery = true)
    Optional<Object[]> findPostWithFile(@Param("postId") int postId);

    Page<Post> findByDisplayAtBetween(
        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
        
    Page<Post> findByTitleContainingAndDisplayAtBetween(
        String title, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
        
    Page<Post> findByContentContainingAndDisplayAtBetween(
        String content, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
        
    Page<Post> findByTitleContainingOrContentContainingAndDisplayAtBetween(
        String title, String content, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
