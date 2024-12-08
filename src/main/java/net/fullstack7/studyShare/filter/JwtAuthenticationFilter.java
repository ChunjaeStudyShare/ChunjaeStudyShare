package net.fullstack7.studyShare.filter;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.security.JwtAuthenticationToken;
import net.fullstack7.studyShare.util.JwtUtil;
import net.fullstack7.studyShare.service.token.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.extern.log4j.Log4j2;
import jakarta.servlet.http.Cookie;
import net.fullstack7.studyShare.exception.TokenException;
@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            log.info("추출된 토큰: {}", token);
            
            if (token != null && tokenService.isTokenValid(token)) {
                String userId = jwtUtil.getUserId(token);
                log.info("유효한 토큰, 사용자 ID: {}", userId);
                
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                    token,
                    userId,
                    Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("SecurityContext에 인증 정보 설정됨, 사용자 ID: {}", userId);
            } else {
                log.info("토큰이 유효하지 않거나 null입니다.");
                if (!shouldNotFilter(request)) {
                    throw new TokenException("유효하지 않은 토큰입니다.");
                }
            }
        } catch (Exception e) {
            log.error("토큰 검증 중 예외 발생", e);
            SecurityContextHolder.clearContext();
            if (!shouldNotFilter(request)) {
                throw e;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        
        // 개발 중에는 모든 요청 허용
        if(true) {
            return true;
        }

        // 정적 리소스와 공개 페이지는 필터링하지 않음
        return path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/assets/") ||
               path.equals("/favicon.ico") ||
               path.equals("/") ||
               path.equals("/member/login") ||
               path.equals("/member/register") ||
               path.equals("/member/find-password") ||
               path.equals("/member/reset-password") ||
               path.startsWith("/admin/") ||
               // 에러 파라미터가 있는 로그인 페이지도 필터링하지 않음
               (path.startsWith("/member/login") && request.getQueryString() != null && request.getQueryString().contains("error=true"));
    }

} 