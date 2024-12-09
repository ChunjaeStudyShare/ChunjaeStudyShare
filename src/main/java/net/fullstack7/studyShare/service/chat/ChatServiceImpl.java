package net.fullstack7.studyShare.service.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;

import net.fullstack7.studyShare.repository.ChatMemberRepository;
import net.fullstack7.studyShare.repository.ChatMessageRepository;
import net.fullstack7.studyShare.repository.ChatRoomRepository;
import net.fullstack7.studyShare.repository.MemberRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    private final SimpMessagingTemplate messagingTemplate;

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

    @Transactional
    @Override
    public int createChatRoom(String userId, String[] invited) {
        ChatRoom newChatRoom = new ChatRoom();
        chatRoomRepository.save(newChatRoom);
        chatMemberRepository.save(ChatMember.builder()
                .member(Member.builder().userId(userId).build())
                .chatRoom(newChatRoom)
                .build()
        );

        for(String id : invited) {
            chatMemberRepository.save(ChatMember.builder()
                    .member(Member.builder().userId(id).build())
                    .chatRoom(newChatRoom)
                    .build()
            );
        }

        chatMessageRepository.save(ChatMessage.builder()
                .sender(Member.builder().userId("chatmanager").build())
                .message("채팅 시작")
                .isRead(1)
                .chatRoom(newChatRoom)
                .build());
        return 0;
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

    @Transactional
    @Override
    public void exitRoom(int roomId, String userId) {
        chatMemberRepository.deleteMember(roomId, userId);
        ChatMessage exitMessage = ChatMessage.builder()
                .sender(Member.builder().userId("chatmanager").build())
                .message(userId + " 님이 나가셨습니다.")
                .isRead(1)
                .chatRoom(ChatRoom.builder().id(roomId).build())
                .build();
        chatMessageRepository.save(exitMessage);
        messagingTemplate.convertAndSend(exitMessage);
    }

    @Override
    @Transactional
    public String inviteUserToChatRoom(int roomId, String userId) {
        if(memberRepository.existsById(userId)){
            Member member = Member.builder().userId(userId).build();
            if(chatRoomRepository.existsById(roomId)){
                ChatRoom chatRoom = ChatRoom.builder().id(roomId).build();
                if(chatMemberRepository.existsByMemberAndChatRoom(Member.builder().userId(userId).build(), ChatRoom.builder().id(roomId).build())){
                    return "이미 참여 중인 회원입니다.";
                }
                ChatMember chatMember = ChatMember.builder().member(member).chatRoom(chatRoom).joinAt(LocalDateTime.now()).build();
                chatMemberRepository.save(chatMember);
                if(chatMember.getId() == 0) {
                    return "다시 시도해주세요.";
                }

                ChatMessage inviteMessage = ChatMessage.builder()
                        .sender(Member.builder().userId("chatmanager").build())
                        .message(userId + " 님이 입장하셨습니다.")
                        .isRead(1)
                        .chatRoom(chatRoom)
                        .build();

                chatMessageRepository.save(inviteMessage);

                messagingTemplate.convertAndSend("/room/"+roomId, inviteMessage);
                return userId + "님을 초대했습니다.";
            }
            return "존재하지 않는 채팅방입니다.";
        }
        return "존재하지 않는 회원입니다.";
    }

    @Override
    public boolean enterChatRoom(int roomId, String userId) {
        return chatMemberRepository.existsByMemberAndChatRoom(Member.builder().userId(userId).build(), ChatRoom.builder().id(roomId).build());
    }


}
