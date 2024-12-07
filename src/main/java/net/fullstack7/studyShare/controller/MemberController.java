package net.fullstack7.studyShare.controller;

import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

/* 리다이렉트용 회원 관련 컨트롤러
 * 회원관련 요청은 api 서버에서 처리 /api/auth/ 또는 /api/user/ 로 요청
 * 회원가입, 로그인, 비밀번호 변경 등 회원 관련 요청은 모두 api 서버에서 처리
 * 리다이렉트 용도및 프론트엔드 렌더링 용도
 * 회원전용 서비스이기때문에 미로그인시 로그인페이지로 리다이렉트
 */
@Controller
@RequiredArgsConstructor
@Log4j2
public class MemberController {
    @GetMapping("/")
    public String index() {
        return "main/login";
    }
}
