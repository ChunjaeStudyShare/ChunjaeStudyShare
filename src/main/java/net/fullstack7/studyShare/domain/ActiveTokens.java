package net.fullstack7.studyShare.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Index;
import java.time.LocalDateTime;

@Entity
@Table(name = "ActiveTokens", indexes = {
    @Index(name = "idx_userId", columnList = "userId"),
    @Index(name = "idx_jti", columnList = "jti"),
    @Index(name = "idx_userId_jti", columnList = "userId,jti"),
    @Index(name = "idx_expiresAt", columnList = "expiresAt")
})
public class ActiveTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "jti", nullable = false, columnDefinition = "VARCHAR(255)")
    private String jti;

    @Column(name = "expiresAt", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime expiresAt;

    @Column(name = "createdAt", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false, columnDefinition = "VARCHAR(20)")
    private Member member;
}
