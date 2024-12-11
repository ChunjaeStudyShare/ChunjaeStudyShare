package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.domain.Share;
import net.fullstack7.studyShare.dto.post.PostShareDTO;
import net.fullstack7.studyShare.service.FriendService;
import net.fullstack7.studyShare.service.post.PostServiceIf;
import net.fullstack7.studyShare.service.share.ShareServiceIf;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/share")
@RequiredArgsConstructor
@Log4j2
public class ShareController {

    private final PostServiceIf postService;
    private final FriendService friendService;
    private final ShareServiceIf shareService;

    @GetMapping("/searchUserIdById")
    @ResponseBody
    public List<PostShareDTO> searchUserIdById(@RequestParam String searchId,
                                               @RequestParam String postId,
                                               HttpServletRequest request){
        //String userId = (String) request.getAttribute("userId");
//        log.info("searchId: {}", searchId);
//        log.info("postId: {}",  postId);
        String userId = "user1";
        List<String> friendList = friendService.list(userId);
        List<String> searchList = friendService.searchUsersById(userId, searchId);

        //검색된 사용자 중에서 내 친구인 항목만 필터링
        List<PostShareDTO> friendCheckedList = new ArrayList<>();

        for (String search : searchList) {
            PostShareDTO dto = new PostShareDTO();
            System.out.println("friend: " + search);
            if(search == null){
                continue;
            }
            if (!friendList.contains(search)) {
                continue; // 친구가 아니면 건너뜀
            }

            for (String friend : friendList) {
                if (search != null && search.equals(friend)) {
                    boolean isShared =  shareService.isSharedByUser(search, postId);
                    dto.setIsShared(isShared ? 1 : 0);
                    if(search.equals(friend)){
                        dto.setUserId(friend);
                    }
                }
            }
            friendCheckedList.add(dto);
        }
        return friendCheckedList;
    }

    @PostMapping("/shareRequest")
    @ResponseBody
    public ResponseEntity<?> shareRequest(@RequestBody PostShareDTO postShareDTO,
                                          HttpServletRequest request) {
        System.out.println(postShareDTO.getUserId());
        String userId = (String) request.getAttribute("userId");
        boolean result = shareService.shareRequest(postShareDTO, userId);
        if(result){
            log.info("share에 추가됨");
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(400).build();
        }

    }

    @PostMapping("/shareCancelRequest")
    @ResponseBody  // 응답을 JSON으로 반환하도록 설정
    public ResponseEntity<?> shareCancelRequest(@RequestBody PostShareDTO postShareDTO) {
        String userId = "user1"; //세션아이디
        boolean result = shareService.shareCancelRequest(postShareDTO, userId);
        if(result) {
            log.info("share에서 삭제됨");
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }






}
