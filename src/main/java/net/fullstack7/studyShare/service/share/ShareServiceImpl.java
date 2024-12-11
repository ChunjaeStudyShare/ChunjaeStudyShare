package net.fullstack7.studyShare.service.share;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.domain.Share;
import net.fullstack7.studyShare.dto.post.*;
import net.fullstack7.studyShare.mapper.FriendMapper;
import net.fullstack7.studyShare.mapper.ShareMapper;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.repository.PostRepository;
import net.fullstack7.studyShare.repository.ShareRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ShareServiceImpl implements ShareServiceIf{
    private final FriendMapper friendMapper;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ShareRepository shareRepository;
    private final ShareMapper shareMapper;
    private final ModelMapper modelMapper;

    @Override
    public Boolean isSharedByUser(String userId, String postId) {
        return friendMapper.isSharedByUser(userId, postId);
    }

    @Override
    public Boolean shareRequest(PostShareDTO postShareDTO, String userId) {
        // 이미 공유되었는지 확인
        //boolean result = shareRepository.existsByPostAndUser(postShareDTO.getPostId(), userId);
        // 공유 받는 자
        Member member = memberRepository.findById(postShareDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        Post post = postRepository.findById(postShareDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보가 없습니다."));
        if(postShareDTO != null){
            try{
                Share share = Share.builder()
                        .createdAt(LocalDateTime.now())
                        .user(Member.builder().userId(postShareDTO.getUserId()).build()) //공유 받는 사람, 객체로 들어와야함
                        .post(post)
                        .build();
//                Post post_in = Post.builder()
//                                .share(1)
//                                .build();
                shareRepository.save(share);
//                postRepository.save(post_in);
                log.info(" 성공  ID: {}", share.getId());
                return true;
            }catch(Exception e){
                log.error("저장 실패: {}", e.getMessage(), e);
                return false;
            }
        }else{
            log.info("dto정보가 없습니다");
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean shareCancelRequest(PostShareDTO postShareDTO, String userId) {
        log.info("dto" + postShareDTO.toString());
        log.info("userId" + userId);
        try {
            // userId로 Member 객체 조회
            Member user = memberRepository.findByUserId(postShareDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

            // postId로 Post 객체 조회
            Post post = postRepository.findById(postShareDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

            // 데이터 삭제
            int result = shareRepository.deleteByUserIdAndPostId(user, post);
            if(result > 0) {
                log.info("삭제 성공");
//                Post post_in = Post.builder()
//                        .share(0)
//                        .build();
//                postRepository.save(post_in);
                return true;
            }else {
                log.info("삭제 실패");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 삭제 실패
        }
    }

//    @Override
//    public List<PostShareDTO> getShareListByPostId(int postId) {
//        return null;
//    }
    @Override
    public List<Share> getShareListByPostId(int postId) {
        List<Share> list = shareRepository.findByPost(Post.builder().id(postId).build());
        return list;
    }



}
