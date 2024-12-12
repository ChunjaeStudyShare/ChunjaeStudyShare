package net.fullstack7.studyShare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.fullstack7.studyShare.domain.ThumbsUp;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.domain.Member;

import java.util.List;

@Repository
public interface ThumbsUpRepository extends JpaRepository<ThumbsUp, Integer> {
    Optional<ThumbsUp> findByPostAndUser(Post post, Member member);
    void deleteByPostAndUser(Post post, Member member);
    int countByPost(Post post);
    boolean existsByPostAndUser(Post post, Member member);

    void deleteByPostIn(List<Post> posts);
    void deleteByUser(Member member);
}
