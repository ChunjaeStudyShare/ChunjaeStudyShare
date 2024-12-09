package net.fullstack7.studyShare.repository;

import jakarta.transaction.Transactional;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.domain.Share;
import net.fullstack7.studyShare.dto.post.PostShareDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShareRepository extends JpaRepository<Share, Integer> {
      //boolean existsByPostAndUser(int postId, String userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Share s WHERE s.user = :user AND s.post = :post")
    int deleteByUserIdAndPostId(@Param("user") Member member , @Param("post") Post post);

    //List<PostShareDTO> findByPostId(@Param("post") Post post);


}
