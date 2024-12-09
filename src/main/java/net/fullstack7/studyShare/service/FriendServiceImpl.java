package net.fullstack7.studyShare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.domain.Share;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.post.PostShareDTO;
import net.fullstack7.studyShare.mapper.FriendMapper;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.repository.PostRepository;
import net.fullstack7.studyShare.repository.ShareRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class FriendServiceImpl implements FriendService {

    private final FriendMapper friendMapper;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ShareRepository shareRepository;

    @Override
    public List<String> list(String userId) {
        List<String> list1 = friendMapper.list1(userId);
        List<String> list2 = friendMapper.list2(userId);
        list1.addAll(list2);

        return list1;
    }

    @Override
    public List<String> searchUsersById(String userId, String searchId) {
        return friendMapper.searchById(userId, searchId);
    }

    @Override
    public Integer amISender(String userId, String searchId) {
        return friendMapper.amISender(userId, searchId);
    }

    @Override
    public Integer amIReceiver(String userId, String searchId) {
        return friendMapper.amIReceiver(userId, searchId);
    }

    @Override
    public Boolean sendFriendRequest(FriendDTO friendDTO) {
        return friendMapper.sendFriendRequest(friendDTO);
    }

    @Override
    public Boolean cancelFriendRequest(FriendDTO friendDTO) {
        return friendMapper.cancelFriendRequest(friendDTO);
    }

    @Override
    public Boolean acceptFriendRequest(FriendDTO friendDTO) {
        return friendMapper.acceptFriendRequest(friendDTO);
    }

    @Override
    public Boolean rejectFriendRequest(FriendDTO friendDTO) {
        return friendMapper.rejectFriendRequest(friendDTO);
    }

    @Override
    public List<String> receivedList(String userId) {
        return friendMapper.receivedList(userId);
    }

    @Override
    public List<String> sentList(String userId) {
        return friendMapper.sentList(userId);
    }

    @Override
    public Boolean isSharedByUser(String userId, String postId) {
        return friendMapper.isSharedByUser(userId, postId);
    }

    @Override
    public Boolean shareRequest(PostShareDTO postShareDTO, String userId) {
        // 이미 공유되었는지 확인

        // 공유 받는 자
        Member member = memberRepository.findById(postShareDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        Post post = postRepository.findById(postShareDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보가 없습니다."));
        if(postShareDTO != null){
            try{
                Share share = Share.builder()
                        .createdAt(LocalDateTime.now())
//                        .requestId(userId) //공유 한 사람
                        .user(Member.builder().userId(postShareDTO.getUserId()).build()) //공유 받는 사람, 객체로 들어와야함
                        .post(post)
                        .build();
                shareRepository.save(share);
                log.info(" 성공  ID: {}", share.getId());
            }catch(Exception e){
                log.error("저장 실패: {}", e.getMessage(), e);
            }

        }
        return false;
    }


}
