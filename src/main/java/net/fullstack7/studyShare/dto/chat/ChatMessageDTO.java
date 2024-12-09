package net.fullstack7.studyShare.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Integer id;
    private String message;
    private LocalDateTime createdAt;
    private Integer isRead;
    private Integer chatRoomId;
    private String senderId;
}
