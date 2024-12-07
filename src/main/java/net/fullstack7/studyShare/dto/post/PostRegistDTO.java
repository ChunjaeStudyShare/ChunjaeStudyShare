package net.fullstack7.studyShare.dto.post;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String title;

    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String content;

    private Integer privacy;

    private Integer share;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime displayAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime displayEnd;

    private LocalDateTime createdAt;

    @Pattern(regexp = "^(.{1,10})(,\\s*.{1,10}){0,3}$", message = "주제 분야는 쉼표로 구분하여 최대 4개까지, 각 키워드는 최대 10자여야 합니다.")
    private String domain;

    @Pattern(regexp = "^(#.{1,10})(,\\s*#.{1,10}){0,3}$", message = "해시태그는 #으로 시작하고 쉼표로 구분하여 최대 4개까지, 각 해시태그는 최대 10자여야 합니다.")
    private String hashtag;

    private Member member;
    private MultipartFile file;
    private String fileName;
    private String path;
    private Post post;
}
