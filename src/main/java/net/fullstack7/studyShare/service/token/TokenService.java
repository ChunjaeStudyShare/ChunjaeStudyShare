package net.fullstack7.studyShare.service.token;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.repository.ActiveTokensRepository;
import net.fullstack7.studyShare.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final ActiveTokensRepository activeTokensRepository;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public boolean isTokenValid(String token) {
        try {
            String userId = jwtUtil.getUserId(token);
            String jti = jwtUtil.getJti(token);
            LocalDateTime now = LocalDateTime.now();
            
            // DB에서만 유효성 검증
            return activeTokensRepository.findValidToken(userId, jti, now).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void invalidateToken(String userId, String jti) {
        // 토큰 즉시 무효화 (로그아웃 등에서 사용)
        activeTokensRepository.deleteByUserIdAndJti(userId, jti);
    }

    // 만료된 토큰 삭제
    @Transactional
    public void cleanupExpiredTokens() {
        activeTokensRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}