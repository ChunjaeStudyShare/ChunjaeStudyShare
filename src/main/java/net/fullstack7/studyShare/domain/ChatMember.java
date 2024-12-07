package net.fullstack7.studyShare.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Member member;
    @Column(columnDefinition = "datetime not null default now() comment '입장시간'")
    private LocalDateTime joinAt;
    @Column(columnDefinition = "datetime not null default now() comment '퇴장시간'")
    private LocalDateTime leaveAt;
}
