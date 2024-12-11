package net.fullstack7.studyShare.service.chat;

import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.dto.chat.ChatRoomDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatService {
    List<ChatRoomDTO> getChatRoomList(String userId) throws IllegalAccessException;
    List<ChatMessage> getChatMessageListByRoomId(int roomId, String userId) throws IllegalAccessException;
    int createChatRoom(String userId, String[] invited);
    ChatMessage addMessageToChatRoom(int roomId, MessageContent message);
    String exitRoom(int roomId, String userId);
    String inviteUserToChatRoom(int roomId, String userId);
    ChatMember chatMemberInfo(int roomId, String userId);
    int isExistChatRoom(List<String> members, String userId);
    boolean leaveChatRoom(int roomId, String userId, LocalDateTime leaveAt);
    boolean enterChatRoom(int roomId, String userId);
}
