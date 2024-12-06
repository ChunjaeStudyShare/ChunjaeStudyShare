package net.fullstack7.studyShare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.MemberDTO;
import net.fullstack7.studyShare.service.FriendService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/list")
    public String list(Model model) {
        String memberId = "test1"; //세션아이디
        List<FriendDTO> friendList = friendService.list(memberId);
        model.addAttribute("friendList", friendList);
        return "friend/list";
    }

    @GetMapping("/find")
    public String find(Model model) {
        return "friend/find";
    }

    @GetMapping("/searchUserById")
    @ResponseBody
    public List<String> searchUserById(@RequestParam String userId) {
        return friendService.searchUsersById(userId);  // 아이디로 검색한 결과를 반환
    }


}
