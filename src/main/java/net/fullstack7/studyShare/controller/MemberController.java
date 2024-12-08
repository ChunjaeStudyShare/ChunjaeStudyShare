package net.fullstack7.studyShare.controller;

import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.springframework.ui.Model;
import net.fullstack7.studyShare.dto.member.PasswordResetRequestDTO;
import net.fullstack7.studyShare.service.member.PasswordResetService;
import net.fullstack7.studyShare.service.member.MemberService;
import net.fullstack7.studyShare.dto.member.MemberResponseDTO;
import net.fullstack7.studyShare.util.SecurityUtil;

/* 리다이렉트용 회원 관련 컨트롤러
 * 회원관련 요청은 api 서버에서 처리 /api/auth/ 또는 /api/user/ 로 요청
 * 회원가입, 로그인, 비밀번호 변경 등 회원 관련 요청은 모두 api 서버에서 처리
 * 리다이렉트 용도및 프론트엔드 렌더링 용도
 * 회원전용 서비스이기때문에 미로그인시 로그인페이지로 리다이렉트
 */
@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/member")
public class MemberController {
    private final PasswordResetService passwordResetService;
    private final SecurityUtil securityUtil;
    private final MemberService memberService;
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/register")
    public String register() {
        return "member/register";
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "member/find-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String token, 
                                  @RequestParam String userId, 
                                  Model model) {
        model.addAttribute("token", token);
        model.addAttribute("userId", userId);
        return "member/reset-password";
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequestDTO request) {
        try {
            passwordResetService.resetPassword(
                request.getUserId(), 
                request.getToken(), 
                request.getNewPassword()
            );
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "비밀번호가 성공적으로 변경되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, 
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/update-user")
    public String updateUser(Model model) {
        String userId = securityUtil.getCurrentUserId();
        MemberResponseDTO member = memberService.findByUserId(userId);
        model.addAttribute("member", member);
        return "member/update-user";
    }

}
