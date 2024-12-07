package net.fullstack7.studyShare.domain;

import jakarta.persistence.Entity;  
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '0: 채팅 중, 1: 채팅 종료'")
    private Integer senderStatus;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '0: 채팅 중, 1: 채팅 종료'")
    private Integer receiverStatus;
}
