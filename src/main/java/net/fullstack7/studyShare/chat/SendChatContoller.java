package net.fullstack7.studyShare.chat;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.service.chat.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendChatContoller {
    private final ChatService chatService;

    @MessageMapping("/{id}")
    @SendTo("/room/{id}")
    public ChatMessage messageContent(@DestinationVariable int id, MessageContent messageContent) throws InterruptedException {
        ChatMessage chatMessage = chatService.addMessageToChatRoom(id, messageContent);
        if(chatMessage != null && chatMessage.getId() > 0) {
            return chatMessage;
        }
        return null;
    }
}
