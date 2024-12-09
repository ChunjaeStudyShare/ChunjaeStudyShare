package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.service.chat.ChatService;
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
    public String chatList(HttpServletRequest request, Model model) {
        String userId = (String) request.getAttribute("userId");

        model.addAttribute("chatlist", chatService.getChatRoomList(userId));

        return "chat/list";
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request, @RequestParam String[] invited, Model model, RedirectAttributes redirectAttributes) {
        String userId = (String) request.getAttribute("userId");
        if(invited.length == 0) {
            redirectAttributes.addFlashAttribute("alertMessage", "채팅방 멤버를 선택하세요");
            return "redirect:/chat/list";
        }

        int roomId = chatService.createChatRoom(userId, invited);

        return "redirect:/chat/room/"+roomId;
    }

    @GetMapping("/room/{id}")
    public String room(HttpServletRequest request, @PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        String userId = (String) request.getAttribute("userId");

        try {
            model.addAttribute("messages", chatService.getChatMessageListByRoomId(id, userId));
            model.addAttribute("roomId", id);
            model.addAttribute("userId", userId);
            return "chat/room";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertMessage", "접근 권한이 없습니다.");
            return "redirect:/chat/list";
        }
    }

    @GetMapping("/room/{id}/exit")
    public String exit(HttpServletRequest request, @PathVariable int id, RedirectAttributes redirectAttributes) {
        String userId = (String) request.getAttribute("userId");

        redirectAttributes.addFlashAttribute("alertMessage", chatService.exitRoom(id, userId));
        return "redirect:/chat/list";
    }

}
