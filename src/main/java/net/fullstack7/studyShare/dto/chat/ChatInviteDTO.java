package net.fullstack7.studyShare.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatInviteDTO {
    private String invitedId;
    private Integer roomId;
}
