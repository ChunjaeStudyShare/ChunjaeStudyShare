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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.awt.SystemColor.info;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private PostServiceIf postService;

    @GetMapping("myList")
    public String myStudyList(){
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
                             HttpSession session, Model model){
        response.setCharacterEncoding("UTF-8");
        //세션 아이디
        String memberId = "user1";
        if(bindingResult.hasErrors()){
            log.error("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("postDTO", dto);
            redirectAttributes.addFlashAttribute("errors", bindingResult);
            return "post/regist";
        }
        // 파일업로드
        String fileName = null;
        try{
            postService.regist(dto, memberId);
            //fileName = CommonFileUtil.uploadFile(dto.getFile());
            return "redirect:/post/myList";
        }catch(Exception e){
            log.info("업로드 실패");
            JSFunc.alert("업로드 실패. 다시 시도해주세요",response);
            return "redirect:/post/regist";
        }
//        if(fileName != null && !fileName.isEmpty()){
//            dto.setFileName(fileName);
//        }

    }
}
