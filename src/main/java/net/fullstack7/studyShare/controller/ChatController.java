package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.chat.MessageContent;
import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.dto.ChatMessageDTO;
import net.fullstack7.studyShare.service.chat.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
@Log4j2
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/list")
    public String chatList(HttpSession session, Model model) {
        String username ="user1";

        model.addAttribute("chatlist", chatService.getChatRoomList(username));
        return "chat/list";
    }

    @GetMapping("/create")
    public String create(HttpSession session, Model model) {
        String username ="user1";
        return "chat/create";
    }

    @GetMapping("/room/{id}")
    public String room(HttpSession session, @PathVariable int id, Model model) {
        model.addAttribute("roomId", id);
        model.addAttribute("messages", chatService.getChatMessageListByRoomId(id));
        return "chat/room";
    }
}
