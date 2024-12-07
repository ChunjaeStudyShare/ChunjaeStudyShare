package net.fullstack7.studyShare.domain;

import io.swagger.v3.oas.annotations.info.Info;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.persistence.Column;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor // JPA 기본 생성자 필요
@AllArgsConstructor // @Builder와 함께 필요
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(50)")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TINYINT(2) DEFAULT 0 COMMENT '오늘의 학습타입(0: 비공개, 1: 공개)'")
    private Integer privacy;

    @Column(columnDefinition = "TINYINT(2) DEFAULT 0 COMMENT '공유 타입(0: 비공개, 1: 공개)'")
    private Integer share;

    @Column(columnDefinition = "DATETIME COMMENT '나의학습 노출 시작 일'")
    private LocalDateTime displayAt;

    @Column(columnDefinition = "DATETIME COMMENT '나의학습 노출 종료 일'")
    private LocalDateTime displayEnd;

    @Column(columnDefinition = "DATETIME COMMENT '게시 일'")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '분야'")
    private String domain;
    
    @Column(columnDefinition = "VARCHAR(50) COMMENT '해시태그'")
    private String hashtag;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Member member;

    @Column(columnDefinition = "VARCHAR(100) COMMENT '썸네일명'")
    private String thumbnailName;

    @Column(columnDefinition = "VARCHAR(200) COMMENT '썸네일경로'")
    private String thumbnailPath;

}
