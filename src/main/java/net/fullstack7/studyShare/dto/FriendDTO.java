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
public class FriendDTO {
    private Integer id;
    private Integer status;
    private String friendId;
    private String requesterId;
}
