package net.fullstack7.studyShare.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Getter
@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "friendId")
    private Member friend;

    @ManyToOne
    @JoinColumn(name = "requesterId")
    private Member requester;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0 COMMENT '0: 친구 요청, 1: 친구 수락'")
    private Integer status;
}
