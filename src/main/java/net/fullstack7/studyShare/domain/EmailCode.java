package net.fullstack7.studyShare.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Getter
@Entity
public class EmailCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "userId")
    private Member user;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '인증 코드'")
    private String code;
}