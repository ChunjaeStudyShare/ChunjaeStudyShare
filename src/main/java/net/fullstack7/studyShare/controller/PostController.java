package net.fullstack7.studyShare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.service.post.PostServiceIf;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    @GetMapping("myList")
    public String myStudyList(){
        return "post/list";
    }

    @GetMapping("/regist")
    public String registGet(){
        return "post/regist";
    }

    @PostMapping("/regist")
    public String registPost(){
        //세션 아이디
        return "redirect:/post/myList";
    }
}
