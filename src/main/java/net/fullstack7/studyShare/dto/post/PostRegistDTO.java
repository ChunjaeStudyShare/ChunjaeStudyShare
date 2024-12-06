package net.fullstack7.studyShare.dto.post;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.domain.Post;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class PostRegistDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer privacy;
    private Integer share;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime displayAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime displayEnd;
    private LocalDateTime createdAt;
    private String domain;
    private String hashtag;
    private Member member;
    private MultipartFile file;
    private String fileName;
    private String path;
    private Post post;
}
