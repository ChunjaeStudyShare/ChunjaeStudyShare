package net.fullstack7.studyShare.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class PostShareDTO {
    private String id;
    private Integer postId;
    private LocalDateTime createAt;
    private String userId; // 공유 받은 사람
    private String  requestId; // 공유 한 사람
    private int isShared;
}
