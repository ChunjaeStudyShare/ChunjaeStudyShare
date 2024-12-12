package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.chat.ChatInviteDTO;
import net.fullstack7.studyShare.dto.chat.ChatLeaveDTO;
import net.fullstack7.studyShare.service.FriendService;
import net.fullstack7.studyShare.service.chat.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatApiController {
    private final ChatService chatService;
    private final FriendService friendService;

    @PostMapping("/room/invite")
    public ResponseEntity<String> invite(@RequestBody ChatInviteDTO chatInviteDTO) {
        try {
            String invitedId = chatInviteDTO.getInvitedId();
            Integer roomId = chatInviteDTO.getRoomId();
            String invitemsg = chatService.inviteUserToChatRoom(roomId, invitedId);
            return ResponseEntity.ok(invitemsg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/room/leave")
    public ResponseEntity<?> leave(@RequestBody ChatLeaveDTO chatLeaveDTO, HttpServletRequest request) {
        try {
            LocalDateTime leaveAt = chatLeaveDTO.getLeaveAt().plusHours(9);
            Integer roomId = chatLeaveDTO.getRoomId();
//            log.info("leaveAt: " + leaveAt);
//            log.info("roomId: " + roomId);
            if(chatService.leaveChatRoom(roomId, (String)request.getAttribute("userId"), leaveAt)) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.ok(false);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            List<String> friends = friendService.list(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
