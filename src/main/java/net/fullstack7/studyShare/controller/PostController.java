package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.post.PostDTO;
import net.fullstack7.studyShare.dto.post.PostRegistDTO;
import net.fullstack7.studyShare.dto.post.PostViewDTO;
import net.fullstack7.studyShare.service.post.PostServiceIf;
import net.fullstack7.studyShare.util.CommonFileUtil;
import net.fullstack7.studyShare.util.JSFunc;
import net.fullstack7.studyShare.util.Paging;
import net.fullstack7.studyShare.util.ValidateList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.awt.SystemColor.info;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Log4j2
public class PostController {

    private final PostServiceIf postService;

    @GetMapping("/myList")
    public String myStudyList(Model model,
                              HttpServletResponse response,
                              @RequestParam(defaultValue = "1") int pageNo,
                              @RequestParam(defaultValue = "10") int pageSize,
                              @RequestParam(required = false) String searchCategory,
                              @RequestParam(required = false) String searchValue,
                              @RequestParam(required = false) String sortType
                              //@RequestParam(required = false) LocalDateTime displayAt,
                              //@RequestParam(required = false) LocalDateTime displayEnd
                              ){
        response.setCharacterEncoding("utf-8");
        String userId = "user1";
        if (!ValidateList.validateMyListParameters(pageNo, searchCategory, searchValue,  response)) {
            return null;
        }
        int totalCnt = postService.totalCnt(searchCategory, searchValue, userId, "createdAt", null, null);
        log.info("totalCnt: " + totalCnt);
        Paging paging = new Paging(pageNo, pageSize, 5, totalCnt);
        List<PostDTO> posts =  postService.selectAllPost(pageNo, pageSize, searchCategory, searchValue, userId, "createdAt", null, null);

        model.addAttribute("posts", posts);
        model.addAttribute("paging", paging);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("uri", "/post/myList");
        return "post/list";
    }

    @GetMapping("/view")
    public String view(Model model,
                             HttpServletResponse response,
                             HttpServletRequest request,
                             @RequestParam String id){
        response.setCharacterEncoding("utf-8");
        PostViewDTO post = postService.findPostWithFile(id);
        if(post != null){
            model.addAttribute("post", post);
            return "post/view";
        }else {
            JSFunc.alertBack("일치하는 ID 정보가 없습니다.",response);
        }
        return null;
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

    @GetMapping("/modify")
    public String modifyGet(@RequestParam String id, Model model, HttpServletResponse response){
        response.setCharacterEncoding("utf-8");

        // 데이터베이스에서 Post 정보 가져오기
        PostViewDTO post = postService.findPostWithFile(id);
        if (post != null) {
            model.addAttribute("post", post);
            return "post/modify";
        } else {
            JSFunc.alertBack("해당 학습 정보를 찾을 수 없습니다.", response);
        }
        return null;
    }


    @PostMapping("/modify")
    public String modifyPost(
                             @Valid PostRegistDTO dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             Model model) {
        response.setCharacterEncoding("utf-8");
        String userId = "user1";
        System.out.println(dto.getDisplayAt());
        System.out.println(dto.getDisplayEnd());

        // 작성자 확인
        if (!postService.checkWriter(dto.getId(), userId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
            return "redirect:/post/myList"; // 권한 없으면 목록으로 리다이렉트
        }

        if (bindingResult.hasErrors()) {
            log.error("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("errors", bindingResult);
            return "redirect:/post/myList";
        }
        try{
            postService.modifyPost(dto, userId);
            return "redirect:/post/myList";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            log.info("수정 실패" + e.getMessage());
            return "post/view";
        }
    }
}
