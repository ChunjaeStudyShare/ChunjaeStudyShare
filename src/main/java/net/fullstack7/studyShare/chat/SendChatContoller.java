package net.fullstack7.studyShare.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.service.chat.ChatService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SendChatContoller {
    private final ChatService chatService;
//    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/{id}")
    @SendTo("/room/{id}")
    public ChatMessage messageContent(@DestinationVariable int id, @Payload MessageContent messageContent) {

        if (messageContent.getContent().length() > 300) {
            throw new IllegalArgumentException("300자 이내 전송 가능합니다.");
        }
        if (messageContent.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        if (messageContent.getSender().isBlank() || messageContent.getSender().length()>20) {
            throw new IllegalArgumentException("발신자 오류로 전송이 불가합니다.");
        }

        ChatMessage chatMessage = chatService.addMessageToChatRoom(id, messageContent);
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
