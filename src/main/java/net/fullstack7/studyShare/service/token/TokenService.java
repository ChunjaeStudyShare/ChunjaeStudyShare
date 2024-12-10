package net.fullstack7.studyShare.service.token;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.repository.ActiveTokensRepository;
import net.fullstack7.studyShare.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.exception.TokenException;
import lombok.extern.log4j.Log4j2;
import java.time.LocalDateTime;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


@Log4j2
@Service
@RequiredArgsConstructor
public class TokenService {
    private final ActiveTokensRepository activeTokensRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public boolean isTokenValid(String token) {
        try {
            Claims claims = jwtUtil.parseClaims(token);
            return validateToken(token);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    private boolean validateToken(String token) {
        try {
            String userId = jwtUtil.getUserId(token);
            String jti = jwtUtil.getJti(token);
            LocalDateTime now = LocalDateTime.now();
            log.info("Token validation - userId: {}, jti: {}, now: {}", userId, jti, now);
            
            Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            
            boolean isValid = activeTokensRepository.findValidToken(member, jti, now).isPresent();
            log.info("Token validation result: {}", isValid);
            
            return isValid;
        } catch (Exception e) {
            log.error("Token validation error", e);
            return false;
        }
    }

    @Transactional
    public void invalidateToken(String token) {
        try {
            String userId = jwtUtil.getUserId(token);
            String jti = jwtUtil.getJti(token);
            Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            
            activeTokensRepository.deleteByUserIdAndJti(member, jti);
        } catch (TokenException e) {
            throw new RuntimeException("토큰 무효화 실패", e);
        }
    }

    @Transactional
    public void cleanupExpiredTokens() {
        activeTokensRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    @Transactional
    public void invalidateAllTokens(String userId) {
        Member member = memberRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        activeTokensRepository.deleteByMember(member);
    }
}
