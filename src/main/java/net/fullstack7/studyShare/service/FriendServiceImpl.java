package net.fullstack7.studyShare.service;

import jakarta.transaction.Transactional;
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
    public boolean deleteFriend(FriendDTO friendDTO) {
        boolean first = friendMapper.deleteFriend1(friendDTO);
        if(first){
            return true;
        } else {
            return friendMapper.deleteFriend2(friendDTO);
        }
    }

    @Override
    public boolean deleteShare(FriendDTO friendDTO) {
        boolean gotShared = friendMapper.deleteShared1(friendDTO);
        if(gotShared){
            return true;
        } else {
            return friendMapper.deleteShared2(friendDTO);
        }
    }


}
