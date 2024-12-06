package net.fullstack7.studyShare.dto;

import java.time.LocalDateTime;

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
