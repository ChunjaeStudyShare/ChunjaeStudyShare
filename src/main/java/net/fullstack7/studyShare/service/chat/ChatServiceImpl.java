package net.fullstack7.studyShare.service.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;

import net.fullstack7.studyShare.mapper.ChatMemberMapper;
import net.fullstack7.studyShare.repository.ChatMemberRepository;
import net.fullstack7.studyShare.repository.ChatMessageRepository;
import net.fullstack7.studyShare.repository.ChatRoomRepository;
import net.fullstack7.studyShare.repository.MemberRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    private final ChatMemberMapper chatMemberMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<ChatRoom> getChatRoomList(String userId) {
        Optional<Member> member = memberRepository.findById(userId);
        return chatRoomRepository.findAll();
    }

    @Override
    public List<ChatMessage> getChatMessageListByRoomId(int roomId, String userId) throws IllegalAccessException {
        ChatMember chatMember = enterChatRoom(roomId, userId);
        if (chatMember == null) {
            throw new IllegalAccessException("채팅방 멤버가 아닙니다.");
        }

        LocalDateTime joinDate = chatMember.getJoinAt();

        return chatMessageRepository.findByChatRoomAndCreatedAtGreaterThanEqual(chatMember.getChatRoom(), joinDate);
    }

    @Transactional
    @Override
    public int createChatRoom(String userId, String[] invited) {
        ChatRoom newChatRoom = ChatRoom.builder().createdAt(LocalDateTime.now()).build();
        chatRoomRepository.save(newChatRoom);

        LocalDateTime joinDate = LocalDateTime.now();

        chatMemberRepository.save(ChatMember.builder()
                .member(Member.builder().userId(userId).build())
                .chatRoom(newChatRoom)
                .joinAt(joinDate)
                .build()
        );

        for (String id : invited) {
            if (memberRepository.existsById(id)) {
                chatMemberRepository.save(ChatMember.builder()
                        .member(Member.builder().userId(id).build())
                        .chatRoom(newChatRoom)
                        .joinAt(joinDate)
                        .build()
                );
            }
        }

        chatMessageRepository.save(ChatMessage.builder()
                .senderId("chatmanager")
                .message("채팅 시작")
                .isRead(1)
                .chatRoom(newChatRoom)
                .createdAt(LocalDateTime.now())
                .build());

        return newChatRoom.getId();
    }


    @Override
    public ChatMessage addMessageToChatRoom(int roomId, MessageContent message) {
        ChatMessage entity = ChatMessage.builder()
                .chatRoom(ChatRoom.builder().id(roomId).build())
                .message(message.getContent())
                .senderId(message.getSender())
                .createdAt(LocalDateTime.now()).build();
        return chatMessageRepository.save(entity);
    }

    @Transactional
    @Override
    public String exitRoom(int roomId, String userId) {
        ChatMember chatMember = enterChatRoom(roomId, userId);
        if (chatMember == null) {
            return "채팅방 멤버가 아닙니다.";
        }

        Member member = memberRepository.findById(userId).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);

        if (member == null) {
            return "회원이 아닙니다.";
        }
        if (chatRoom == null) {
            return "존재하지 않는 채팅방입니다.";
        }

        chatMemberRepository.deleteByChatRoomAndMember(chatRoom, member);
        if (chatMemberRepository.countByChatRoom(chatRoom) == 0) {
            chatMessageRepository.deleteAllByChatRoom(chatRoom);
            chatRoomRepository.deleteById(roomId);
            return "채팅방에서 퇴장하셨습니다.";
        }
        ChatMessage exitMessage = ChatMessage.builder()
                .senderId("chatmanager")
                .message(userId + " 님이 나갔습니다.")
                .isRead(1)
                .chatRoom(chatRoom)
                .createdAt(LocalDateTime.now())
                .build();
        chatMessageRepository.save(exitMessage);
        messagingTemplate.convertAndSend("/room/" + roomId, exitMessage);
        return "채팅방에서 퇴장하셨습니다.";
    }

    @Override
    @Transactional
    public String inviteUserToChatRoom(int roomId, String userId) {
        if (memberRepository.existsById(userId)) {
            Member member = Member.builder().userId(userId).build();
            if (chatRoomRepository.existsById(roomId)) {
                ChatRoom chatRoom = ChatRoom.builder().id(roomId).build();
                if (chatMemberRepository.existsByMemberAndChatRoom(Member.builder().userId(userId).build(), ChatRoom.builder().id(roomId).build())) {
                    return "이미 참여 중인 회원입니다.";
                }
                ChatMember chatMember = ChatMember.builder().member(member).chatRoom(chatRoom).joinAt(LocalDateTime.now()).build();
                chatMemberRepository.save(chatMember);
                if (chatMember.getId() == 0) {
                    return "다시 시도해주세요.";
                }

                ChatMessage inviteMessage = ChatMessage.builder()
                        .senderId("chatmanager")
                        .message(userId + " 님이 입장하셨습니다.")
                        .isRead(1)
                        .chatRoom(chatRoom)
                        .createdAt(LocalDateTime.now())
                        .build();

                chatMessageRepository.save(inviteMessage);

                messagingTemplate.convertAndSend("/room/" + roomId, inviteMessage);
                return userId + "님을 초대했습니다.";
            }
            return "존재하지 않는 채팅방입니다.";
        }
        return "존재하지 않는 회원입니다.";
    }

    @Override
    public ChatMember enterChatRoom(int roomId, String userId) {
        return chatMemberRepository.findByMemberAndChatRoom(Member.builder().userId(userId).build(), ChatRoom.builder().id(roomId).build()).orElse(null);
    }

    @Override
    public boolean isExistChatRoom(int roomId, String[] members) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);
        if (chatRoom == null) {
            Set<String> chatMembers = chatMemberMapper.getChatRoomMembers(roomId);
            for (String userId : members) {
                chatMembers.remove(userId);
            }
            return chatMembers.isEmpty();
        }
        return false;
    }


}
