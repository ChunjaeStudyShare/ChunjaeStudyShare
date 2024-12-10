package net.fullstack7.studyShare.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
    private Integer chatRoomId;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private List<String> members;
    private int newMessagesCount;
}


