package net.fullstack7.studyShare.service.chat;

import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;

import java.util.List;

public interface ChatService {
    List<ChatRoom> getChatRoomList(String userId);
    List<ChatMessage> getChatMessageListByRoomId(int roomId, String userId) throws IllegalAccessException;
    int createChatRoom(String userId, String[] invited);
    ChatMessage addMessageToChatRoom(int roomId, MessageContent message);
    String exitRoom(int roomId, String userId);
    String inviteUserToChatRoom(int roomId, String userId);
    ChatMember enterChatRoom(int roomId, String userId);
    boolean isExistChatRoom(int roomId, String[] members);
}
