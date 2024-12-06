package net.fullstack7.studyShare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.mapper.FriendMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class FriendServiceImpl implements FriendService {

    private final FriendMapper friendMapper;

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


}