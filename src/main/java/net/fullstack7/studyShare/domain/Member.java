package net.fullstack7.studyShare.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column(columnDefinition = "VARCHAR(20) COMMENT '아이디'")
    private String userId;

    @Column(columnDefinition = "VARCHAR(10) COMMENT '이름'")
    private String name;

    @Column(length = 64, columnDefinition = "TEXT COMMENT '솔트'")
    private String salt;

    @Column(unique = true, columnDefinition = "VARCHAR(50) COMMENT '이메일'")
    private String email;

    @Column(length = 128, columnDefinition = "VARCHAR(128) COMMENT '비밀번호'")
    private String password;

    @Column(unique = true, columnDefinition = "VARCHAR(15) COMMENT '휴대폰 번호'")
    private String phone;

    @Column(columnDefinition = "TINYINT(2) DEFAULT 3 COMMENT '0: 활동 중, 1: 휴면, 2: 탈퇴(강퇴), 3: 미인증, 4: 잠금'")
    private Integer status;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '마지막 로그인 시간'")
    private LocalDateTime lastLogin;

    @Column(columnDefinition = "INT DEFAULT 0 COMMENT '로그인 시도 횟수 최대 5회까지 가능'")
    private Integer loginTry;
}
