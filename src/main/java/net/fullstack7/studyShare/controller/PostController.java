package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Share;
import net.fullstack7.studyShare.dto.post.*;
import net.fullstack7.studyShare.service.post.PostServiceIf;
import net.fullstack7.studyShare.service.share.ShareServiceIf;
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
import net.fullstack7.studyShare.util.LogUtil;


@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Log4j2
public class PostController {

    private final PostServiceIf postService;
    private final ShareServiceIf shareService;

    @GetMapping("/myList")
    public String myStudyList(Model model,
                              HttpServletResponse response,
                              @Valid PostPagingDTO dto){
        LogUtil logUtil = new LogUtil();
        logUtil.info("dto: " + dto);
        response.setCharacterEncoding("utf-8");
        String userId = "user1";
        // if (!ValidateList.validateMyListParameters(pageNo, searchCategory, searchValue, displayAt, displayEnd, sortType, response)) {
        //     return null;
        // }
        int totalCnt = postService.totalCnt(dto.getSearchCategory(), dto.getSearchValue(), userId, dto.getSortType(), dto.getDisplayAt(), dto.getDisplayEnd());
        log.info("totalCnt: " + totalCnt);
        Paging paging = new Paging(dto.getPageNo(), dto.getPageSize(), dto.getBlockSize(), totalCnt);
        List<PostDTO> posts =  postService.selectAllPost(dto.getPageNo(), dto.getPageSize(), dto.getSearchCategory(), dto.getSearchValue(), userId, dto.getSortType(), dto.getDisplayAt(), dto.getDisplayEnd());

        model.addAttribute("posts", posts);
        model.addAttribute("paging", paging);
        model.addAttribute("postPagingDTO", dto);
        // model.addAttribute("searchCategory", dto.getSearchCategory());
        // model.addAttribute("searchValue", dto.getSearchValue());
        // model.addAttribute("sortType", dto.getSortType());
        // model.addAttribute("displayAt", dto.getDisplayAt());
        // model.addAttribute("displayEnd", dto.getDisplayEnd());
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
        //공유 목록 가져오기
        List<Share> shareList = shareService.getShareListByPostId(Integer.parseInt(id));
        log.info("aaaaaaa" + shareList.toString());
        model.addAttribute("shareList", shareList);
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

    @GetMapping("/shareList")
    public String shareList(Model model,
                            HttpServletResponse response,
                            @Valid PostSharePagingDTO dto) {
        LogUtil logUtil = new LogUtil();
        logUtil.info("dto: " + dto);
        response.setCharacterEncoding("utf-8");
        String userId = "user1";

        int totalCnt = postService.totalCnt(dto.getSearchCategory(), dto.getSearchValue(), userId, dto.getSortType(), dto.getDisplayAt(), dto.getDisplayEnd());
        log.info("totalCnt: " + totalCnt);
        List<PostShareDTO> sharePosts = postService.getSharedPosts(dto, userId);
        Paging paging = new Paging(dto.getPageNo(), dto.getPageSize(), dto.getBlockSize(), totalCnt);
        System.out.println("sharePosts: " + sharePosts.size());
        log.info(sharePosts.toString());
        model.addAttribute("posts", sharePosts);
        model.addAttribute("paging", paging);
        model.addAttribute("postPagingDTO", dto);
        model.addAttribute("uri", "/post/shareList");
        return "post/shareList";

    }
    @GetMapping("/shareList_1")
    public String shareList_1(Model model,
                            HttpServletResponse response,
                            @Valid PostSharePagingDTO dto) {
        LogUtil logUtil = new LogUtil();
        logUtil.info("dto: " + dto);
        response.setCharacterEncoding("utf-8");
        String userId = "user1";

        int totalCnt = postService.totalCnt(dto.getSearchCategory(), dto.getSearchValue(), userId, dto.getSortType(), dto.getDisplayAt(), dto.getDisplayEnd());
        log.info("totalCnt: " + totalCnt);
        List<PostMyShareDTO> sharePosts = postService.selectPostsByUserId(dto, userId);
        Paging paging = new Paging(dto.getPageNo(), dto.getPageSize(), dto.getBlockSize(), totalCnt);
        System.out.println("sharePosts: " + sharePosts.size());
        log.info(sharePosts.toString());

        model.addAttribute("posts", sharePosts);
        model.addAttribute("paging", paging);
        model.addAttribute("postSharePagingDTO", dto);
        model.addAttribute("uri", "/post/shareList_1");
        return "post/shareList_1";

    }
    //인규가 작업함
    @GetMapping("/delete")
    public String delete(@RequestParam int id, HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        boolean result = postService.delete(id);
        if (result) {
            JSFunc.alertLocation("삭제 성공","/post/myList",response);
            return null;
        } else {
            JSFunc.alertBack("삭제 실패",response);
            return null;
        }

    }
}
