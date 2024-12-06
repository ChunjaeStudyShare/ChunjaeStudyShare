package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.service.chat.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/{id}")
    @SendTo("/room/{id}")
    public MessageContent sendMessage(@DestinationVariable int id, MessageContent content, HttpSession session) throws InterruptedException {

//        session.getAttribute() sessionId

        String username = "user1";

        return new MessageContent(username, content.getContent());
    }

    @GetMapping("/list")
    public String chatList(HttpSession session, Model model) {
        String username ="user1";

        model.addAttribute("chatlist", chatService.getChatRoomList(username));
        return "list";
    }
}
