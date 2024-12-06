package net.fullstack7.studyShare.service.chat;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.repository.ChatMessageRepository;
import net.fullstack7.studyShare.repository.ChatRoomRepository;
import net.fullstack7.studyShare.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ChatRoom> getChatRoomList(String userId) {
        Optional<Member> member = memberRepository.findById(userId);
        return member.map(chatRoomRepository::findBySender).orElse(null);
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

}
