package net.fullstack7.studyShare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import net.fullstack7.studyShare.util.SecurityUtil;
import net.fullstack7.studyShare.exception.TokenException;
import net.fullstack7.studyShare.util.LogUtil;

@Controller
public class RootController {
    // private final SecurityUtil securityUtil;
    @GetMapping("/")
    public String index() {
        try {
            String userId = SecurityUtil.getCurrentUserId();
            return "today/main";
        } catch (TokenException e) {
            return "main/index";
        }
    }
}
