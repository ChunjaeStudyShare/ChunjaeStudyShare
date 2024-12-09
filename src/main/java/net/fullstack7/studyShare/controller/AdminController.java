package net.fullstack7.studyShare.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.admin.AdminDTO;
import net.fullstack7.studyShare.service.admin.AdminService;
import net.fullstack7.studyShare.service.member.MemberService;
import net.fullstack7.studyShare.dto.member.MemberDTO;
import org.springframework.ui.Model;
import net.fullstack7.studyShare.dto.admin.PageResponseDTO;
import net.fullstack7.studyShare.dto.admin.PageRequestDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import net.fullstack7.studyShare.util.PageUrlBuilder;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final PageUrlBuilder pageUrlBuilder;

    @GetMapping("/login")
    public String goLogin() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(AdminDTO adminDTO, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if(adminService.login(adminDTO.getAdminId(),adminDTO.getPassword())) {
                session.setAttribute("admin", adminDTO.getAdminId());
                return "redirect:/admin/dashboard";
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/login";
        }
        return "redirect:/admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String goDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String goUsers(
        @Validated PageRequestDTO pageRequestDTO, 
        BindingResult bindingResult,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            // 검증 실패시 기본값으로 처리
            pageRequestDTO = new PageRequestDTO();
        }

        PageResponseDTO<MemberDTO> pageResponse = memberService.getMembersByPaging(pageRequestDTO);
        
        model.addAttribute("pageResponse", pageResponse);
        model.addAttribute("pageUrlBuilder", pageUrlBuilder);

        return "admin/users";
    }

    @GetMapping("/users/modify/")
    public String goModify(@RequestParam("currentPage") String currentPage, @RequestParam("userId") String userId, Model model) {
        try {
            MemberDTO memberDTO = memberService.getMemberById(userId);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("member", memberDTO);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
        return "admin/users-modify";
    }
    @PostMapping("/users/modify")
    public String modify(MemberDTO memberDTO, @RequestParam("currentPage") String currentPage,RedirectAttributes redirectAttributes) {
        try{
            String url[] = currentPage.split("currentPage=");
            System.out.println("url[1]: " + url[1]);
            memberService.updateMember(memberDTO);
            redirectAttributes.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
            return "redirect:/admin/users?page=" + url[1];
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
    }
}
