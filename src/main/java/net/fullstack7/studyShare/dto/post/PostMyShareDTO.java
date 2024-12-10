package net.fullstack7.studyShare.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class PostMyShareDTO {
    private String id;
    private Integer postId;
    private String title;
    private LocalDateTime createdAt; //게시글 등록 시간
    private String userId; //로그인된 사용자 이름
    private int isShared;
    private List<PostShareDTO> shares; //공유자 목록
}
