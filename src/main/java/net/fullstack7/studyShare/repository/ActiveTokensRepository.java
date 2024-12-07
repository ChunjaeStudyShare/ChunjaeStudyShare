package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.ActiveTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ActiveTokensRepository extends JpaRepository<ActiveTokens, Integer> {
    @Query("SELECT t FROM ActiveTokens t WHERE t.user.userId = :userId AND t.jti = :jti AND t.expiresAt > :now")
    Optional<ActiveTokens> findValidToken(String userId, String jti, LocalDateTime now);

    void deleteByExpiresAtBefore(LocalDateTime now);

    void deleteByUserIdAndJti(String userId, String jti);
}