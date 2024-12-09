package net.fullstack7.studyShare.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

import lombok.*;
import jakarta.persistence.Column;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "DATETIME COMMENT '공유 일'")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "COMMENT '공유 받은 사람'")
    private String requestId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

}
