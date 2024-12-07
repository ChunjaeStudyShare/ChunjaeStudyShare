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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
@Log4j2
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/list")
    public String chatList(HttpSession session, Model model) {
        String userId ="user1";

        model.addAttribute("chatlist", chatService.getChatRoomList(userId));
        return "chat/list";
    }

    @PostMapping("/create")
    public String create(HttpSession session, String[] invited, Model model) {
        String userId = "user1";

        chatService.createChatRoom(userId, invited);

        return "redirect:/chat/room";
    }

    @GetMapping("/room/{id}")
    public String room(HttpSession session, @PathVariable int id, Model model) {
        String userId = "user1";
        model.addAttribute("roomId", id);
        model.addAttribute("userId", userId);
        model.addAttribute("messages", chatService.getChatMessageListByRoomId(id));
        return "chat/room";
    }

    @GetMapping("/room/{id}/exit")
    public String exit(HttpSession session, @PathVariable int id, RedirectAttributes redirectAttributes) {
        String userId = "user1";
        chatService.exitRoom(id, userId);
        redirectAttributes.addFlashAttribute("errors", "채팅방에서 퇴장하셨습니다.");
        return "redirect:/chat/list";
    }
}
