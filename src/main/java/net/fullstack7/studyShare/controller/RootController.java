package net.fullstack7.studyShare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.util.JwtUtil;
import net.fullstack7.studyShare.service.token.TokenService;

@Log4j2
@Controller
@RequiredArgsConstructor
public class RootController {
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    /*
    루트 페이지
    */
    @GetMapping("/")
    public String index(HttpServletRequest request) {
        try {
            String token = jwtUtil.resolveToken(request);
            if (token != null && tokenService.isTokenValid(token)) {
                String userId = jwtUtil.getUserId(token);
                log.info("현재 사용자 ID: {}", userId);
                return "redirect:/today/main";
            }
        } catch (Exception e) {
            log.info("로그인되지 않은 사용자");
            return "redirect:/today/main";
        }
        return "main/index";
    }

}
