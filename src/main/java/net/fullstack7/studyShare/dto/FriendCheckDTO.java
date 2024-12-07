package net.fullstack7.studyShare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Log4j2
public class FriendCheckDTO {
    private String userId;
    private Integer isFriend=0;
    private Integer sent; // 친구 신청 보낸 상황이면 1, 아니면 0
    private Integer received; // 친구 신청 받은 상황이면 1, 아니면 0
}
