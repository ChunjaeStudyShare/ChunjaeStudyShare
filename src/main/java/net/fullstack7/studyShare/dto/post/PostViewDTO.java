package net.fullstack7.studyShare.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Member;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Data
public class PostViewDTO {
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

    // File 테이블 필드
    private Integer postId;
    private String fileName;
    private String path;

}
