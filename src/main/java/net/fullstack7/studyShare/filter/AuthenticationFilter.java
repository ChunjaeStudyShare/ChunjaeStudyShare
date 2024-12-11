package net.fullstack7.studyShare.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.util.JwtUtil;
import net.fullstack7.studyShare.service.token.TokenService;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {
    
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    
    private static final Set<String> EXCLUDED_PATHS = Set.of(
        "/css/*", "/js/*", "/images/*", "/assets/*",
        "/favicon.ico", "/",
        "/member/login", "/member/register",
        "/member/find-password", "/member/reset-password",
        "/admin/*"
    );

    private static final boolean IS_DEVELOPMENT = true;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        log.info("AuthenticationFilter doFilter 호출");
        
        // 개발 모드일 경우 토큰 검증 생략 및 임시 사용자 ID ("user1") 설정
        if (IS_DEVELOPMENT) {
            log.info("개발 모드이므로 인증 제외 경로 체크 통과");
            httpRequest.setAttribute("userId", "user1");
            chain.doFilter(request, response);
            return;
        }

        // 인증 제외 경로 체크
        if (shouldExclude(httpRequest)) {
            log.info("인증 제외 경로 체크 통과");
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(httpRequest);
            log.info("추출된 토큰: {}", token);

            if (token != null && tokenService.isTokenValid(token)) {
                String userId = jwtUtil.getUserId(token);
                log.info("유효한 토큰, 사용자 ID: {}", userId);
                
                // 요청 속성에 인증 정보 저장
                httpRequest.setAttribute("userId", userId);
                chain.doFilter(request, response);
            } else {
                log.info("토큰이 유효하지 않거나 null입니다.");
                handleUnauthorized(httpRequest, httpResponse);
            }
        }
        catch (Exception e) {
            log.error("토큰 검증 중 예외 발생", e);
            handleException(httpRequest, httpResponse);
        }

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
                    log.info("쿠키에서 토큰 찾음: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        log.info("토큰을 찾을 수 없음");
        return null;
    }

    private boolean shouldExclude(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("shouldExclude 호출 - path: {}", path);
        
        // 정적 리소스 및 공개 페이지는 제외
        boolean isExcluded = EXCLUDED_PATHS.stream()
                .anyMatch(excludedPath -> {
                    if (excludedPath.endsWith("/*")) {
                        String basePath = excludedPath.substring(0, excludedPath.length() - 2);
                        return path.startsWith(basePath);
                    }
                    return path.equals(excludedPath);
                });
        
        log.info("인증 제외 여부: {}", isExcluded);
        return isExcluded;
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        if (request.getRequestURI().startsWith("/api/")) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"message\":\"인증이 필요한 요청입니다.\"}");
        } else {
            String encodedMessage = URLEncoder.encode("세션이 만료되었거나 인증이 필요한 요청입니다.", StandardCharsets.UTF_8);
            response.sendRedirect("/member/login?error=true&message=" + encodedMessage);
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.sendRedirect("/error");
    }
}