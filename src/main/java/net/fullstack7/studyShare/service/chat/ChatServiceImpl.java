package net.fullstack7.studyShare.service.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.dto.ChatMessageDTO;
import net.fullstack7.studyShare.mapper.ChatMessageMapper;
import net.fullstack7.studyShare.repository.ChatMemberRepository;
import net.fullstack7.studyShare.repository.ChatMessageRepository;
import net.fullstack7.studyShare.repository.ChatRoomRepository;
import net.fullstack7.studyShare.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    @Override
    public List<ChatRoom> getChatRoomList(String userId) {
        Optional<Member> member = memberRepository.findById(userId);
        return chatRoomRepository.findAll();
    }

    @Override
    public List<ChatMessage> getChatMessageListByRoomId(int roomId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        return chatRoom.map(chatMessageRepository::findByChatRoom).orElse(null);
    }

    @Override
    public int createChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    @Override
    public ChatMessage addMessageToChatRoom(int roomId, MessageContent message) {
        ChatMessage entity = ChatMessage.builder()
                .chatRoom(ChatRoom.builder().id(roomId).build())
                .message(message.getContent())
                .sender(Member.builder().userId(message.getSender()).build())
                .createdAt(LocalDateTime.now()).build();
        return chatMessageRepository.save(entity);
    }


}
