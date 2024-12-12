package net.fullstack7.studyShare.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.service.chat.ChatService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SendChatContoller {
    private final ChatService chatService;

    @MessageMapping("/{id}")
    @SendTo("/room/{id}")
    public ChatMessage messageContent(@DestinationVariable String id, @Payload MessageContent messageContent) {

        if(id.isBlank() || !id.matches("^\\d+$") || id.length() > 9) {
            throw new IllegalArgumentException("존재하지 않는 채팅방입니다. 채팅방 주소를 확인하거나 새로 생성해주세요.");
        }

        int roomId = Integer.parseInt(id);

        if(chatService.chatMemberInfo(roomId, messageContent.getSender()) == null) {
            throw new IllegalArgumentException("이 채팅방에 참여하고 있지 않습니다. 참여 후에 메시지를 확인하거나 보낼 수 있습니다.");
        }
        if (messageContent.getContent().length() > 300) {
            throw new IllegalArgumentException("메시지는 300자 이내로 작성해주세요.");
        }
        if (messageContent.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        if (messageContent.getSender().isBlank() || messageContent.getSender().length()>20) {
            throw new IllegalArgumentException("회원 ID가 올바르지 않습니다. 다시 시도해주세요.");
        }

        ChatMessage chatMessage = chatService.addMessageToChatRoom(roomId, messageContent);
        if (chatMessage != null && chatMessage.getId() > 0) {
            return chatMessage;
        }
        return null;
    }

    @MessageExceptionHandler
    @SendToUser(destinations="/queue/errors", broadcast=false)
    public IllegalArgumentException handleException(IllegalArgumentException exception) {
        return exception;
    }
}
