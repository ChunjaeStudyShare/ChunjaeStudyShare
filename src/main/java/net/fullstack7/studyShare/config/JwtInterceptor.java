package net.fullstack7.studyShare.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.util.JwtUtil;
import net.fullstack7.studyShare.service.token.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import groovy.util.logging.Log4j2;

/**
 * 토큰 유효성 검증 및 userId 설정
 * 
 * 사용 예제
 * @GetMapping("/some-endpoint")
 * public String someEndpoint() {
 *     try {
 *         String userId = SecurityUtil.getCurrentUserId();
 *         // userId를 이용한 로직
 *         return "success-page";
 *     } catch (TokenExceoption e) {
 *         return "main/login";
 *     }
 * }
 * 
 * 직접 접근도 가능 (권장하지 않음)
 * @GetMapping("/some-endpoint")
 * public String someEndpoint(HttpServletRequest request) {
 *     String userId = (String) request.getAttribute("userId");
 * }
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("JwtInterceptor 호출");
        String token = extractToken(request);
        
        if (token != null && tokenService.isTokenValid(token)) {
            String userId = jwtUtil.getUserId(token);
            request.setAttribute("userId", userId);
            return true;
        }
        
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        System.out.println("JwtInterceptor 호출 실패");
        return false;
    }

    /**
     * 토큰 추출
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}