package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Post;
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
import org.springframework.data.jpa.repository.query.JSqlParserUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import net.fullstack7.studyShare.service.ThumbsUp.ThumbsUpService;
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
    private final ThumbsUpService thumbsUpService;

    @GetMapping("/myList")
    public String myStudyList(Model model,
                              HttpServletResponse response,
                              HttpServletRequest request,
                              @Valid PostPagingDTO dto){
        LogUtil logUtil = new LogUtil();
        logUtil.info("dto: " + dto);
        response.setCharacterEncoding("utf-8");
        String userId = (String) request.getAttribute("userId");
        int totalCnt = postService.totalCnt(dto.getSearchCategory(), dto.getSearchValue(), userId, dto.getSortType(), dto.getDisplayAt(), dto.getDisplayEnd());
        Paging paging = new Paging(dto.getPageNo(), dto.getPageSize(), dto.getBlockSize(), totalCnt);
        List<PostDTO> posts =  postService.selectAllPost(dto.getPageNo(), dto.getPageSize(), dto.getSearchCategory(), dto.getSearchValue(), userId, dto.getSortType(), dto.getDisplayAt(), dto.getDisplayEnd());
        model.addAttribute("posts", posts);
        model.addAttribute("paging", paging);
        model.addAttribute("postPagingDTO", dto);
        model.addAttribute("uri", "/post/myList");
        return "post/list";
    }

    @GetMapping("/view")
    public String view(Model model,
                             HttpServletResponse response,
                             HttpServletRequest request,
                             @RequestParam(value = "currentPage", defaultValue = "") String currentPage,
                             @RequestParam(defaultValue = "") String type,
                             @RequestParam String id,
                             RedirectAttributes redirectAttributes) {
            response.setCharacterEncoding("utf-8");
            log.info("current  {}" , currentPage);
        String userId = (String) request.getAttribute("userId");
//            log.info("type {}", type);
//            log.info("id {}", id);

            try{
                //게시글 조회
                PostViewDTO post = postService.findPostWithFile(id);
                if(post != null){
                    //권한 확인(작성자인지, 공유받은 사람인지)
                    boolean hasAccess = postService.isOwnerOrSharedWithUser(Integer.parseInt(id), userId);
                    if (!hasAccess) {
                        redirectAttributes.addFlashAttribute("alertMessage", "접근 권한이 없습니다.");
                        return "redirect:/post/shareList";
                    }
                    // 좋아요 개수 조회
                    Integer thumbUpCnt = thumbsUpService.countThumbsUp(Integer.parseInt(id));
                    model.addAttribute("thumbsUpCnt", thumbUpCnt);
                  
                    //공유 목록 조회
                    List<Share> shareList = shareService.getShareListByPostId(Integer.parseInt(id));
                    model.addAttribute("shareList", shareList);
                    model.addAttribute("post", post);
                    model.addAttribute("currentPage", currentPage);
                    if("share".equals(type)){
                        return "post/shareView";
                    }else{
                        return "post/view";
                    }
                }else {
                    redirectAttributes.addFlashAttribute("alertMessage", "게시글 정보가 없습니다.");
                    if("share".equals(type)){
                        return "redirect:/post/shareList"; //이전페이지로 돌려야할듯
                    }else{
                        return "redirect:/post/myList";
                    }
                }
            }catch(Exception e){
                redirectAttributes.addFlashAttribute("alertMessage",  e.getMessage());
                return "redirect:/post/shareList";
            }
        }

    @GetMapping("/regist")
    public String registGet(@RequestParam("currentPage") String currentPage,Model model){
        model.addAttribute("currentPage", currentPage);
        return "post/regist";
    }

    @PostMapping("/regist")
    public String registPost(@ModelAttribute @Valid PostRegistDTO dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             HttpServletRequest request,
                             HttpSession session, Model model) {
        response.setCharacterEncoding("UTF-8");
        // 세션 아이디
        String userId = (String) request.getAttribute("userId");
        if (bindingResult.hasErrors()) {
            log.error("Validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("dto", dto);
            redirectAttributes.addFlashAttribute("alertMessage", bindingResult);
            return "post/regist";
        }
        try {
            postService.regist(dto, userId);
            return "redirect:/post/myList";
        } catch (IOException e) {
            log.info("aaa");
            redirectAttributes.addFlashAttribute("alertMessage",  "업로드 실패 다시 시도해주세요");
            return "post/regist";
        } catch (Exception e) {
            log.info("bbbbb");
            redirectAttributes.addFlashAttribute("alertMessage",  "업로드 실패 다시 시도해주세요");
            return "post/regist";
        }
    }

    @GetMapping("/modify")
    public String modifyGet(@RequestParam String id, Model model,
                            HttpServletResponse response,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes){
        response.setCharacterEncoding("utf-8");
        String userId = (String) request.getAttribute("userId");
        // 작성자 확인
        if (!postService.checkWriter(Integer.parseInt(id), userId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "수정 권한이 없습니다.");
            return "redirect:/post/myList"; // 권한 없으면 목록으로 리다이렉트
        }
        PostViewDTO post = postService.findPostWithFile(id);
        if (post != null) {
            model.addAttribute("post", post);
            return "post/modify";
        } else {
            redirectAttributes.addFlashAttribute("alertMessage",  "해당 학습 정보를 찾을 수 없습니다.");
            return "redirect:post/myList";
        }
    }

    @PostMapping("/modify")
    public String modifyPost(
                             @Valid PostRegistDTO dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse response,
                             HttpServletRequest request,
                             Model model) {
        response.setCharacterEncoding("utf-8");
        String userId = (String) request.getAttribute("userId");

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
                            HttpServletRequest request,
                            @Valid PostSharePagingDTO dto) {
        // LogUtil logUtil = new LogUtil();
        //logUtil.info("dto: " + dto);
        response.setCharacterEncoding("utf-8");
        String userId = (String) request.getAttribute("userId");
        int totalCnt = postService.totalCnt(dto.getSearchCategory(), dto.getSearchValue(), userId, dto.getSortType(), dto.getDisplayAt(), dto.getDisplayEnd());
        //log.info("totalCnt: " + totalCnt);
        List<PostMyShareDTO> sharePosts;
        List<PostShareDTO> getSharePosts;
        if("share".equals(dto.getSortType()) || dto.getSortType().isEmpty()){
            sharePosts = postService.selectPostsByUserId(dto, userId); // 공유 한 것
            model.addAttribute("posts", sharePosts);
            model.addAttribute("sortType", "share");
        }else{
            getSharePosts = postService.getSharedPosts(dto, userId); // 공유 받은 것
            log.info(getSharePosts.toString());
            model.addAttribute("posts", getSharePosts);
            model.addAttribute("sortType", "receiveShare");
        }
        Paging paging = new Paging(dto.getPageNo(), dto.getPageSize(), dto.getBlockSize(), totalCnt);
        model.addAttribute("paging", paging);
        model.addAttribute("postPagingDTO", dto);
        model.addAttribute("uri", "/post/shareList");
        return "post/shareList";
    }

    //인규가 작업함
    //2024-12-10 수미 수정
    @GetMapping("/delete")
    public String delete(@RequestParam int id, HttpServletResponse response, RedirectAttributes redirectAttributes){
        response.setCharacterEncoding("utf-8");
        String userId = "user1"; //세션 아이디
        //로그인한 회원이 작성한 글인지 확인
        if (!postService.checkWriter(id, userId)) {
            redirectAttributes.addFlashAttribute("alertMessage", "삭제 권한이 없습니다.");
            return "redirect:/post/myList";
        }
        //게시글이 존재하는지 확인
        Optional<Post> isPost = postService.findPostById(id);
        if(isPost.isEmpty()){
            redirectAttributes.addFlashAttribute("alertMessage", "게시글이 존재하지 않습니다.");
            return "redirect:/post/myList";
        }else{
            boolean result = postService.delete(id);
            if (result) {
                redirectAttributes.addFlashAttribute("alertMessage", "게시글이 삭제되었습니다");
                return "redirect:/post/myList";
            } else {
                redirectAttributes.addFlashAttribute("alertMessage", "게시글 삭제 실패. 다시 시도하세요");
                return null;
            }
        }
    }
}
