package net.fullstack7.studyShare.service.ThumbsUp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.fullstack7.studyShare.repository.ThumbsUpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.ThumbsUp;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.repository.PostRepository;
import net.fullstack7.studyShare.exception.CustomException;

@Service
@Log4j2
@RequiredArgsConstructor
public class ThumbsUpService {
    private final ThumbsUpRepository thumbsUpRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public boolean insertThumbsUp(Integer postId, String userId) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        
        if (!isThumbsUp(post, member)) {
            thumbsUpRepository.save(ThumbsUp.builder().user(member).post(post).build());
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteThumbsUp(Integer postId, String userId) {
        try{
            Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            
            Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
            
            thumbsUpRepository.deleteByPostAndUser(post, member);
        } catch (Exception e) {
            log.error("좋아요 삭제 처리 중 오류가 발생했습니다: {}", e.getMessage());
            throw new RuntimeException("좋아요 삭제 처리 중 오류가 발생했습니다");
        }
    }

    public int countThumbsUp(Integer postId) {
        try{
            Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
            return thumbsUpRepository.countByPost(post);
        } catch (Exception e) {
            log.error("좋아요 카운트 처리 중 오류가 발생했습니다: {}", e.getMessage());
            throw new RuntimeException("좋아요 카운트 처리 중 오류가 발생했습니다");
        }
    }

    private boolean isThumbsUp(Post post, Member member) {
        return thumbsUpRepository.existsByPostAndUser(post, member);
    }
}