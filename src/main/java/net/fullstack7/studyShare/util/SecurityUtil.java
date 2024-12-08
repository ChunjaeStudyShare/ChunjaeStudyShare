package net.fullstack7.studyShare.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import net.fullstack7.studyShare.exception.TokenException;
import net.fullstack7.studyShare.security.JwtAuthenticationToken;


/**
 * 현재 요청의 userId 반환
 */

@Component
public class SecurityUtil {
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new TokenException("인증 정보가 없습니다.");
        }
        
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getUserId();
        }
        
        throw new TokenException("잘못된 인증 정보입니다.");
    }
}