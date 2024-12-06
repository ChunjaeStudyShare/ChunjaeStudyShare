package net.fullstack7.studyShare.dto;

import jakarta.persistence.*;
import net.fullstack7.studyShare.domain.Member;

import java.time.LocalDateTime;

public class PostDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer privacy;
    private Integer share;
    private LocalDateTime displayAt;
    private LocalDateTime displayEnd;
    private LocalDateTime createdAt;
    private String domain;
    private String hashtag;
    private Member member;
}
