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
public class ShareInfoDTO {
    private String sharedUserId;       // 공유자 ID
    private LocalDateTime sharedAt;    // 공유한 시간
    private Integer postId;
}
