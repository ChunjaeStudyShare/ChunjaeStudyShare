package net.fullstack7.studyShare.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageContent {
    @Size(min = 1, max = 300, message = "300자 이하 전송 가능합니다.")
    private String content;
    @NotBlank
    private String sender;
}


