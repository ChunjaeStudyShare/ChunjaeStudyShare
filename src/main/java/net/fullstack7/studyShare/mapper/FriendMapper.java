package net.fullstack7.studyShare.mapper;

import net.fullstack7.studyShare.domain.Friend;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.post.PostShareDTO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface FriendMapper {
    List<String> list1(String userId);
    List<String> list2(String userId);
    List<String> searchById(String userId, String searchId);
    Integer amISender(String userId, String searchId);
    Integer amIReceiver(String userId, String searchId);
    Boolean sendFriendRequest(FriendDTO friendDTO);
    Boolean cancelFriendRequest(FriendDTO friendDTO);
    Boolean acceptFriendRequest(FriendDTO friendDTO);
    Boolean rejectFriendRequest(FriendDTO friendDTO);
    List<String> receivedList(String userId);
    List<String> sentList(String userId);

    Boolean isSharedByUser(String userId, String postId);
    Boolean deleteFriend1(FriendDTO friendDTO);
    Boolean deleteFriend2(FriendDTO friendDTO);
    Boolean deleteShared1(FriendDTO friendDTO);
    Boolean deleteShared2(FriendDTO friendDTO);
    List<Integer> postIdList(String userId);

}
