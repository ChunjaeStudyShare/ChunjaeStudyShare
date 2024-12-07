package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.post.PostDTO;
import net.fullstack7.studyShare.dto.post.PostRegistDTO;
import net.fullstack7.studyShare.service.post.PostServiceIf;
import net.fullstack7.studyShare.util.CommonFileUtil;
import net.fullstack7.studyShare.util.JSFunc;
import net.fullstack7.studyShare.util.ValidateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static java.awt.SystemColor.info;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Log4j2
public class PostController {

    private final PostServiceIf postService;

    @GetMapping("myList")
    public String myStudyList(Model model,
                              HttpServletResponse response,
                              @RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(required = false) String searchCategory,
                              @RequestParam(required = false) String searchValue,
                              @RequestParam(required = false) String sortType,
                              @RequestParam(required = false) String sortOrder){
        response.setCharacterEncoding("utf-8");
        if (!ValidateList.validateMyListParameters(pageNo, searchCategory, searchValue,  response)) {
            return null;
        }
        return "post/list";
    }

    @GetMapping("/regist")
    public String registGet(){
        return "post/regist";
    }

    @PostMapping("/regist")
    public String registPost(@ModelAttribute @Valid PostRegistDTO dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             HttpSession session, Model model) {
        response.setCharacterEncoding("UTF-8");
        // 세션 아이디
        String userId = "user1";
        if (bindingResult.hasErrors()) {
            log.error("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("errors", bindingResult);
            return "post/regist";
        }

        try {
            postService.regist(dto, userId);
            return "redirect:/post/myList";
        } catch (IOException e) {
            log.error("PostController - 업로드 실패: {}", e.getMessage(), e);
            //model.addAttribute("errorMessage", e.getMessage());
            JSFunc.alert("업로드 실패. 다시 시도해주세요" + e.getMessage(), response);
        } catch (Exception e) {
            log.error("PostController - 예기치 않은 오류: {}", e.getMessage(), e);
            //model.addAttribute("errorMessage", e.getMessage());
            JSFunc.alert("예기치 않은 오류가 발생했습니다." + e.getMessage(), response);
        }
        return "redirect:/post/regist";
    }

}
