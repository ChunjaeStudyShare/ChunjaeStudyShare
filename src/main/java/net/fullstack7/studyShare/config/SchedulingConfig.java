package net.fullstack7.studyShare.config;

import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.service.token.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {
    private final TokenService tokenService;
/**
 *  " * * * * * *" 순서대로 초 분 시 일 월 요일
 *  0 0 * * * * : 매 시간 0분 0초에 실행
 * 
 * ex) "0 0 0 * * *" : 매일 0시 0분 0초에 실행
 * "0 5 0 * * *" : 매일 0시 5분에 실행
 * "0 0 12 * * *" : 매일 12시 0분 0초에 실행
 * "0 0 12 * * 1" : 매주 월요일 12시 0분 0초에 실행
 * "0 0 12 * * 1-5" : 매주 월요일부터 금요일까지 12시 0분 0초에 실행
 */
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {
        tokenService.cleanupExpiredTokens();
    }
}