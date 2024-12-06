package net.fullstack7.studyShare.chat;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageContent {
    private String content;
    private String sender;
}


