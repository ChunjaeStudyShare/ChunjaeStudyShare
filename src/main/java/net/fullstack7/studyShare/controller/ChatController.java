package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.exception.CustomException;
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
    public String chatList(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        String userId = (String) request.getAttribute("userId");

        try {
            model.addAttribute("chatlist", chatService.getChatRoomList(userId));
            return "chat/list";
        } catch (IllegalAccessException e) {
            redirectAttributes.addFlashAttribute(e.getMessage());
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @PostMapping("/create")
    public String create(HttpServletRequest request, @RequestParam String[] invited, Model model, RedirectAttributes redirectAttributes) {
        String userId = (String) request.getAttribute("userId");
        if(invited.length == 0) {
            redirectAttributes.addFlashAttribute("alertMessage", "채팅방 멤버를 선택하세요");
            return "redirect:/chat/list";
        }

        try {
            int roomId = chatService.createChatRoom(userId, invited);
            return "redirect:/chat/room/"+roomId;
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
            return "redirect:/chat/list";
        }

    }

    @GetMapping("/friend")
    public String friend(HttpServletRequest request, @RequestParam String friendId, Model model, RedirectAttributes redirectAttributes) {
        String userId = (String) request.getAttribute("userId");
        if(friendId.isBlank()) {
            redirectAttributes.addFlashAttribute("alertMessage", "채팅방 멤버를 선택하세요");
            return "redirect:/friend/list";
        }

        int roomId = chatService.createChatRoom(userId, new String[] {friendId});

        return "redirect:/chat/room/"+roomId;
    }

    @GetMapping("/room/{id}")
    public String room(HttpServletRequest request, @PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        String userId = (String) request.getAttribute("userId");

        if(id.isBlank() || !id.matches("^\\d+$") || id.length() > 9) {
            redirectAttributes.addFlashAttribute("alertMessage", "존재하지 않는 채팅방입니다. 채팅방 주소를 확인하거나 새로 생성해주세요.");
            return "redirect:/chat/list";
        }

        int roomId = Integer.parseInt(id);

        try {
            model.addAttribute("messages", chatService.getChatMessageListByRoomId(roomId, userId));
            model.addAttribute("roomId", roomId);
            model.addAttribute("userId", userId);
            chatService.enterChatRoom(roomId, userId);
            return "chat/room";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertMessage", "접근 권한이 없습니다.");
            return "redirect:/chat/list";
        }
    }

    @GetMapping("/room/{id}/exit")
    public String exit(HttpServletRequest request, @PathVariable String id, RedirectAttributes redirectAttributes) {
        if(id.isBlank() || !id.matches("^\\d+$") || id.length() > 9) {
            redirectAttributes.addFlashAttribute("alertMessage", "존재하지 않는 채팅방입니다. 채팅방 주소를 확인하거나 새로 생성해주세요.");
            return "redirect:/chat/list";
        }

        int roomId = Integer.parseInt(id);

        String userId = (String) request.getAttribute("userId");

        redirectAttributes.addFlashAttribute("alertMessage", chatService.exitRoom(roomId, userId));
        return "redirect:/chat/list";
    }

}
