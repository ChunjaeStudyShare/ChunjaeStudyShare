package net.fullstack7.studyShare.service.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;

import net.fullstack7.studyShare.dto.chat.ChatRoomDTO;
import net.fullstack7.studyShare.exception.CustomException;
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

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatMemberRepository chatMemberRepository;

    private final ChatMemberMapper chatMemberMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<ChatRoomDTO> getChatRoomList(String userId) throws IllegalAccessException {
        Optional<Member> member = memberRepository.findById(userId);

        if (member.isPresent()) {
            List<ChatRoomDTO> list = chatMemberMapper.findChatRoomListByUserId(userId);

            list.forEach(room -> {
                room.setMembers(chatMemberMapper.findMembersByChatRoomId(room.getChatRoomId()));
//                log.info(room);
            });
            return list;
        } else {
            throw new IllegalAccessException("회원 정보가 확인되지 않습니다. 다시 로그인하거나 회원 가입 후 이용해주세요.");
        }
    }

    @Override
    public List<ChatMessage> getChatMessageListByRoomId(int roomId, String userId) throws IllegalAccessException {
        ChatMember chatMember = chatMemberInfo(roomId, userId);
        if (chatMember == null) {
            throw new IllegalAccessException("이 채팅방에 참여하고 있지 않습니다. 참여 후에 메시지를 확인하거나 보낼 수 있습니다.");
        }

        LocalDateTime joinDate = chatMember.getJoinAt();

        return chatMessageRepository.findByChatRoomAndCreatedAtGreaterThanEqual(chatMember.getChatRoom(), joinDate);
    }

    @Transactional
    @Override
    public int createChatRoom(String userId, String[] invited) {
        if(invited.length >= 20){
            throw new CustomException("채팅방의 최대 참여 인원은 20명입니다. 초과된 인원은 초대할 수 없습니다.");
        }
        String errorMessage = "";
        if(invited.length==1){
            try{
                log.info("userId: "+userId);
                log.info("invited[0]: "+invited[0]);
                int existRoom = isExistChatRoom(userId,invited[0]);
                log.info("existRoom: "+existRoom);
                if(existRoom>0){
                    log.info("existRoom: "+existRoom);
                    return existRoom;
                }
            } catch (Exception e) {
                log.info("chatServiceImpl.createChatRoom: "+e);
                errorMessage = e.getMessage();
            }
        }

        ChatRoom newChatRoom = ChatRoom.builder().createdAt(LocalDateTime.now()).build();
        chatRoomRepository.save(newChatRoom);

        LocalDateTime joinDate = LocalDateTime.now();

        chatMemberRepository.save(ChatMember.builder()
                .member(Member.builder().userId(userId).build())
                .chatRoom(newChatRoom)
                .joinAt(joinDate)
                .leaveAt(joinDate)
                .build()
        );

        for (String id : invited) {
            if (memberRepository.existsById(id)) {
                chatMemberRepository.save(ChatMember.builder()
                        .member(Member.builder().userId(id).build())
                        .chatRoom(newChatRoom)
                        .joinAt(joinDate)
                        .leaveAt(joinDate)
                        .build()
                );
            }
        }

        chatMessageRepository.save(ChatMessage.builder()
                .senderId("chatmanager")
                .message("채팅방이 생성되었습니다.")
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
        ChatMember chatMember = chatMemberInfo(roomId, userId);
        if (chatMember == null) {
            return "이 채팅방에 참여하고 있지 않습니다. 참여 후에 메시지를 확인하거나 보낼 수 있습니다.";
        }

        Member member = memberRepository.findById(userId).orElse(null);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);

        if (member == null) {
            return "회원 정보가 확인되지 않습니다. 다시 로그인하거나 회원 가입 후 이용해주세요.";
        }
        if (chatRoom == null) {
            return "존재하지 않는 채팅방입니다. 채팅방 ID를 확인하거나 새로 생성해주세요.";
        }

        chatMemberRepository.deleteByChatRoomAndMember(chatRoom, member);

        // 채팅방,메시지도 삭제할 경우
//        if (chatMemberRepository.countByChatRoom(chatRoom) == 0) {
//            chatMessageRepository.deleteAllByChatRoom(chatRoom);
//            chatRoomRepository.deleteById(roomId);
//            return "채팅방에서 퇴장하셨습니다.";
//        }

        //채팅방에 시스템 메시지 전송
        ChatMessage exitMessage = ChatMessage.builder()
                .senderId("chatmanager")
                .message(userId + " 님이 나갔습니다.")
                .isRead(1)
                .chatRoom(chatRoom)
                .createdAt(LocalDateTime.now())
                .build();
        chatMessageRepository.save(exitMessage);
        messagingTemplate.convertAndSend("/room/" + roomId, exitMessage);
        return "채팅방 목록에서 삭제합니다. 더 이상 메시지를 주고받을 수 없습니다.";
    }

    @Override
    @Transactional
    public String inviteUserToChatRoom(int roomId, String userId) {
        if (memberRepository.existsById(userId)) {
            Member member = Member.builder().userId(userId).build();
            if (chatRoomRepository.existsById(roomId)) {
                ChatRoom chatRoom = ChatRoom.builder().id(roomId).build();
                if(chatMemberRepository.countByChatRoom(chatRoom)==20){
                    return "채팅방의 최대 참여 인원은 20명입니다. 초과된 인원은 초대할 수 없습니다.";
                }
                if (chatMemberRepository.existsByMemberAndChatRoom(Member.builder().userId(userId).build(), ChatRoom.builder().id(roomId).build())) {
                    return "해당 회원은 이미 채팅방에 참여 중입니다.";
                }
                LocalDateTime now = LocalDateTime.now();
                ChatMember chatMember = ChatMember.builder().member(member).chatRoom(chatRoom).joinAt(now).leaveAt(now).build();
                chatMemberRepository.save(chatMember);
                if (chatMember.getId() == 0) {
                    return "초대 도중 오류가 발생했습니다. 다시 시도해주세요.";
                }

                ChatMessage inviteMessage = ChatMessage.builder()
                        .senderId("chatmanager")
                        .message(userId + " 님이 초대되었습니다.")
                        .isRead(1)
                        .chatRoom(chatRoom)
                        .createdAt(now)
                        .build();

                chatMessageRepository.save(inviteMessage);

                messagingTemplate.convertAndSend("/room/" + roomId, inviteMessage);
                return userId + "님을 초대했습니다.";
            }
            return "존재하지 않는 채팅방입니다. 채팅방 ID를 확인하거나 새로 생성해주세요.";
        }
        return "회원 정보가 확인되지 않습니다. 다시 로그인하거나 회원 가입 후 이용해주세요.";
    }

    @Override
    public ChatMember chatMemberInfo(int roomId, String userId) {
        return chatMemberRepository.findByMemberAndChatRoom(Member.builder().userId(userId).build(), ChatRoom.builder().id(roomId).build()).orElse(null);
    }

    @Override
    public int isExistChatRoom(String user1, String user2) {
        if(user1.equals(user2)) {
            log.info("user1: "+user1);
            log.info("user2: "+user2);
            throw new IllegalArgumentException("대상을 선택한 후 메시지를 보내주세요.");
        }
        log.info("user1: "+user1);
        log.info("user2: "+user2);
        return chatMemberMapper.findChatRoomIdBy2UserId(user1, user2);
    }

    @Override
    public boolean leaveChatRoom(int roomId, String userId, LocalDateTime leaveAt) {
        ChatMember chatMember = chatMemberInfo(roomId, userId);
        if (chatMember == null) {
            return false;
        }
        int result = chatMemberMapper.updateLeaveAt(leaveAt, roomId, userId);

        return result > 0;
    }

    @Override
    public boolean enterChatRoom(int roomId, String userId) {
        return chatMemberMapper.updateLeaveAt(null, roomId, userId) > 0;
    }

    @Override
    public List<String> getChatMemberList(int roomId) {
        return chatMemberMapper.findMembersByChatRoomId(roomId);
    }


}
