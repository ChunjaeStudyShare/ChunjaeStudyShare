package net.fullstack7.studyShare.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
@Entity
public class QnA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT COMMENT '질문'")
    private String question;

    @Column(columnDefinition = "TEXT COMMENT '답변'")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "questionerId")
    private Member questioner;
}
