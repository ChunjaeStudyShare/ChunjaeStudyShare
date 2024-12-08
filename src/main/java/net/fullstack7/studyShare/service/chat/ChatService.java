package net.fullstack7.studyShare.service.chat;

import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.dto.ChatMessageDTO;

import java.util.List;

public interface ChatService {
    List<ChatRoom> getChatRoomList(String userId);
    List<ChatMessage> getChatMessageListByRoomId(int roomId);
    int createChatRoom(String userId, String[] invited);
    ChatMessage addMessageToChatRoom(int roomId, MessageContent message);
    void exitRoom(int roomId, String userId);
    String inviteUserToChatRoom(int roomId, String userId);
    boolean enterChatRoom(int roomId, String userId);
}
