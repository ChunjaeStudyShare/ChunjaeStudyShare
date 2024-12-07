package net.fullstack7.studyShare.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import net.fullstack7.studyShare.exception.TokenException;

/**
 * 현재 요청의 userId 반환
 */
public class SecurityUtil {
    public static String getCurrentUserId() throws TokenException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        
        HttpServletRequest request = attributes.getRequest();
        Object userId = request.getAttribute("userId");
        if (userId == null) {
            throw TokenException.tokenExpired();
        }
        return userId.toString();
    }
}