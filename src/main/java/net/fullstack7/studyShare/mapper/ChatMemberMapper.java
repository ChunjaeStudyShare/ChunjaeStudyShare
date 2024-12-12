package net.fullstack7.studyShare.mapper;

import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.dto.chat.ChatRoomDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper
public interface ChatMemberMapper {
    Set<String> findChatRoomMembers(Integer chatRoomId);
    int updateLeaveAt(LocalDateTime leaveAt, Integer chatRoomId, String userId);
    List<ChatRoomDTO> findChatRoomListByUserId(String userId);
    List<String> findMembersByChatRoomId(Integer chatRoomId);
    Integer findChatRoomIdBy2UserId(String user1, String user2);
}
