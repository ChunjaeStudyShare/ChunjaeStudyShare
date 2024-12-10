package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.ActiveTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import net.fullstack7.studyShare.domain.Member;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ActiveTokensRepository extends JpaRepository<ActiveTokens, Integer> {
    @Query("SELECT t FROM ActiveTokens t WHERE t.member = :member AND t.jti = :jti AND t.expiresAt > :now")
    Optional<ActiveTokens> findValidToken(@Param("member") Member member, @Param("jti") String jti, @Param("now") LocalDateTime now);

    void deleteByExpiresAtBefore(LocalDateTime now);

    @Query("DELETE FROM ActiveTokens t WHERE t.member = :member AND t.jti = :jti")
    void deleteByUserIdAndJti(@Param("member") Member member, @Param("jti") String jti);

    @Query("DELETE FROM ActiveTokens t WHERE t.member = :member")
    void deleteByMember(@Param("member") Member member);

    Optional<ActiveTokens> findByUserId(String userId);
}
