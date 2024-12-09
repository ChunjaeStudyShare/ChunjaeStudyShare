package net.fullstack7.studyShare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.ChatInviteDTO;
import net.fullstack7.studyShare.service.chat.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatApiController {
    private final ChatService chatService;

    @PostMapping("/room/invite")
    public ResponseEntity<String> invite(@RequestBody ChatInviteDTO chatInviteDTO) {
        try {
            String invitedId = chatInviteDTO.getInvitedId();
            Integer roomId = chatInviteDTO.getRoomId();
            log.info("invite: " + invitedId);
            log.info("roomId: " + roomId);
            String invitemsg = chatService.inviteUserToChatRoom(roomId, invitedId);
            return ResponseEntity.ok(invitemsg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
