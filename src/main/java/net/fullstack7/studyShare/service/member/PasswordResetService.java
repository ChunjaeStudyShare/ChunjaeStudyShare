package net.fullstack7.studyShare.service.member;

import net.fullstack7.studyShare.domain.EmailCode;
import net.fullstack7.studyShare.domain.Member;
import net.fullstack7.studyShare.repository.MemberRepository;

import java.util.HashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.log4j.Log4j2;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.repository.EmailCodeRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Log4j2
public class PasswordResetService {
    private final RestTemplate restTemplate;
    private final String API_BASE_URL = "https://api.gyeongminiya.asia";

    public void resetPassword(String userId, String token, String newPassword) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("userId", userId);
            requestBody.put("token", token);
            requestBody.put("newPassword", newPassword);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                API_BASE_URL + "/api/user/reset-password",
                request,
                Map.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("비밀번호 변경에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("Password reset failed", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}