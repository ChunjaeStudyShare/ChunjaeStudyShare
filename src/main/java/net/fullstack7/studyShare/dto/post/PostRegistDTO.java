package net.fullstack7.studyShare.dto.post;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "제목은 필수 입력 값입니다.")
    @Size(min = 2, max = 50, message = "제목은 1자 이상 50자 이하여야 합니다.")
    private String title;

    @NotNull(message = "학습 내용은 필수 입력 값입니다.")
    @Size(min = 10, message = "학습 내용은 최소 10자 이상 100자 이하어야 합니다.")
    private String content;

    @NotNull(message = "오늘의 학습 노출 여부를 선택해 주세요.")
    @Min(value = 0, message = "노출 여부는 '0'(노출 안 함) 또는 '1'(노출)이어야 합니다.")
    @Max(value = 1, message = "노출 여부는 '0'(노출 안 함) 또는 '1'(노출)이어야 합니다.")
    private Integer privacy;

    private Integer share;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") // 노출 여부가 "노출"일 경우 필수
    private LocalDateTime displayAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") // 노출 여부가 "노출"일 경우 필수
    private LocalDateTime displayEnd;

    private LocalDateTime createdAt;

    @Pattern(regexp = "^$|^(.{1,10})(,\\s*.{1,10}){0,3}$", message = "주제 분야는 쉼표로 구분하여 최대 4개까지, 각 키워드는 최대 10자여야 합니다.")
    private String domain;

    @Pattern(regexp = "^$|^(#.{1,10})(,\\s*#.{1,10}){0,3}$", message = "해시태그는 #으로 시작하고 쉼표로 구분하여 최대 4개까지, 각 해시태그는 최대 10자여야 합니다.")
    private String hashtag;

    private Member member;
    private MultipartFile file;
    private String fileName;
    private String path;
    private Post post;
    private String thumbnailName;
    private String thumbnailPath;
    private boolean deleteImage;


}
