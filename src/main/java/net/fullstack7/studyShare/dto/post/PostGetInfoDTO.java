package net.fullstack7.studyShare.dto.post;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Member;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class PostGetInfoDTO {
    //share
    //private String id;
    private Integer postId;
    private LocalDateTime createdAt; //공유일
    private String userId;

    //post
    private Integer id;
    private String title;
    private String content;
    private Integer privacy;
    private Integer share;
    private LocalDateTime displayAt;
    private LocalDateTime displayEnd;
    private LocalDateTime createdAtPost; // 글 작성일
    private String domain;
    private String hashtag;
    private Member member;


}
