package net.fullstack7.studyShare.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT COMMENT '메시지'")
    private String message;
    @Column(columnDefinition = "DATETIME COMMENT '메시지 전송 일'")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '0: 읽지 않음, 1: 읽음'")
    private Integer isRead;

    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "senderId")
    private Member sender;
}
