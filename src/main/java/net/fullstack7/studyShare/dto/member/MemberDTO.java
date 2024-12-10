package net.fullstack7.studyShare.dto.member;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor  
public class MemberDTO {
    private Integer status;
    private LocalDateTime lastLogin;
    private String salt;
    private String password;
    private String email;
    private String name;
    private String phone;
    private String userId;
}
