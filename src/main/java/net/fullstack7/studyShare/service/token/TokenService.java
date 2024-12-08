package net.fullstack7.studyShare.service.token;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.repository.ActiveTokensRepository;
import net.fullstack7.studyShare.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.repository.MemberRepository;
import net.fullstack7.studyShare.util.SecurityUtil;
import net.fullstack7.studyShare.exception.TokenExceoption;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final ActiveTokensRepository activeTokensRepository;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public boolean isTokenValid(String token) {
        try {
            String userId = jwtUtil.getUserId(token);
            String jti = jwtUtil.getJti(token);
            LocalDateTime now = LocalDateTime.now();
            Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            
            return activeTokensRepository.findValidToken(member, jti, now).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void invalidateToken(String token) {
        try {
            String userId = SecurityUtil.getCurrentUserId();
            String jti = jwtUtil.getJti(token);
            Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            
            activeTokensRepository.deleteByUserIdAndJti(member, jti);
        } catch (TokenExceoption e) {
            throw new RuntimeException("토큰 무효화 실패", e);
        }
    }

    @Transactional
    public void cleanupExpiredTokens() {
        activeTokensRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}