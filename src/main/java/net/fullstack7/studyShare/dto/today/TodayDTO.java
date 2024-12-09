package net.fullstack7.studyShare.dto.today;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.dto.post.PostShareDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class TodayDTO {
    private Integer id;
    private String title;
    private String userId;
    private String content;
    private Integer privacy;
    private Integer share;
    private LocalDateTime displayAt;
    private LocalDateTime displayEnd;
    private LocalDateTime createdAt;
    private String domain;
    private String hashtag;
    private List<PostShareDTO> sharedList;
    private String thumbnailPath;
    private String thumbnailName;
}
