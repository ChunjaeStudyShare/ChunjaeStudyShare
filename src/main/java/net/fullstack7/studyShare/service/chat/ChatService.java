package net.fullstack7.studyShare.service.chat;

import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.dto.ChatMessageDTO;

import java.util.List;

public interface ChatService {
    List<ChatRoom> getChatRoomList(String userId);
    List<ChatMessage> getChatMessageListByRoomId(int roomId);
    int createChatRoom(ChatRoom chatRoom);
    ChatMessage addMessageToChatRoom(int roomId, MessageContent message);
}
