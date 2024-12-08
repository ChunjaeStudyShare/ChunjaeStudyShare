package net.fullstack7.studyShare.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        log.error("Unauthorized error: {}", authException.getMessage());
        
        // API 요청인 경우 JSON 응답
        if (request.getRequestURI().startsWith("/api/")) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> data = new HashMap<>();
            data.put("success", false);
            data.put("message", "인증이 필요한 페이지입니다.");
            data.put("path", request.getRequestURI());
            
            response.getWriter().write(objectMapper.writeValueAsString(data));
        } 
        // 이미 로그인 페이지이거나 에러 파라미터가 있는 경우 리다이렉트하지 않음
        else if (!request.getRequestURI().equals("/member/login") && 
                 !(request.getQueryString() != null && request.getQueryString().contains("error=true"))) {
            String encodedMessage = URLEncoder.encode("세션이 만료되었거나 인증이 필요한 요청입니다.", StandardCharsets.UTF_8);
            response.sendRedirect("/member/login?error=true&message=" + encodedMessage);
        }
    }
}