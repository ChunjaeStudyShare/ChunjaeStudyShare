package net.fullstack7.studyShare.domain;

import jakarta.persistence.Entity;  
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "senderId")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private Member receiver;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '0: 채팅 중, 1: 채팅 종료'")
    private Integer senderStatus;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '0: 채팅 중, 1: 채팅 종료'")
    private Integer receiverStatus;
}
