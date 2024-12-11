package net.fullstack7.studyShare.service;

import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.member.MemberDTO;
import net.fullstack7.studyShare.dto.post.PostShareDTO;

import java.util.List;

public interface FriendService {
    List<String> list(String userId);

    List<String> searchUsersById(String userId, String searchId);
    Integer amISender(String userId, String searchId);
    Integer amIReceiver(String userId, String searchId);
    Boolean sendFriendRequest(FriendDTO friendDTO);
    Boolean cancelFriendRequest(FriendDTO friendDTO);
    Boolean acceptFriendRequest(FriendDTO friendDTO);
    Boolean rejectFriendRequest(FriendDTO friendDTO);
    List<String> receivedList(String userId);
    List<String> sentList(String userId);
    boolean deleteFriend(FriendDTO friendDTO);
    boolean deleteShare(FriendDTO friendDTO);

}
