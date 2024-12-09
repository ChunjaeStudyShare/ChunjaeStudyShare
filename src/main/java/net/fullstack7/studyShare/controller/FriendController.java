package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendCheckDTO;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.member.MemberDTO;
import net.fullstack7.studyShare.dto.post.PostShareDTO;
import net.fullstack7.studyShare.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<String> friendList = friendService.list(userId);
        log.info("friendList: {}", friendList);
        model.addAttribute("friendList", friendList);
        return "friend/list";
    }

    @GetMapping("/searchUserIdById")
    @ResponseBody
    public List<PostShareDTO> searchUserIdById(@RequestParam String searchId){
        String userId = "user1"; //세션 아이디
        String postId = "18";
        log.info("searchId: {}", searchId);
        List<String> friendList = friendService.list(userId);
        List<String> searchList = friendService.searchUsersById(userId, searchId);
        System.out.println("friendListSize" + friendList.size());

        // 3. 검색된 사용자 중에서 내 친구인 항목만 필터링
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
                    boolean isShared =  friendService.isSharedByUser(search, postId);
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
    public ResponseEntity<?> shareRequest(@RequestBody PostShareDTO postShareDTO) {
        String userId = "user1"; //세션아이디
        postShareDTO.setRequestId(userId);
        boolean result = friendService.shareRequest(postShareDTO, userId);
        if(result){
            log.info("share에 추가됨");
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(400).build();
        }

    }


//    @PostMapping("/sendRequest")
//    @ResponseBody  // 응답을 JSON으로 반환하도록 설정
//    public ResponseEntity<?> sendRequest(@RequestBody FriendDTO friendDTO) {
//        String userId = "testuser2"; //세션아이디
//        friendDTO.setRequesterId(userId);
//        friendDTO.setStatus(0);
//        boolean success = friendService.sendFriendRequest(friendDTO);
//        if(success) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.status(400).build();
//        }
//    }

    @GetMapping("/searchUserById")
    @ResponseBody
    public List<FriendCheckDTO> searchUserById(@RequestParam String searchId, HttpServletRequest request) { // 여기에서 검색된 아이디가 있는지 없는지 여부를 확인하려면 ? 만약 null이 나오면 어떤 에러가 뜨는지?
        String userId = (String) request.getAttribute("userId");
        log.info("searchId: {}", searchId);
        List<String> friendList = friendService.list(userId);
        List<String> searchList = friendService.searchUsersById(userId, searchId);

        List<FriendCheckDTO> friendCheckedList = new ArrayList<>();
        for(String search : searchList) {
            FriendCheckDTO friendCheckDTO = new FriendCheckDTO();
            friendCheckDTO.setUserId(search);

            //여기서 나랑 친구 신청이 걸려있는지 여부를 체크해줘야 함.
            friendCheckDTO.setReceived(friendService.amIReceiver(userId, search)); // 내가 받았는지 여부 확인 0이면 없음, 1이면 내가 받은거임.
            friendCheckDTO.setSent(friendService.amISender(userId, search)); // 내가 보냈는지 여부 확인 0이면 없음 1이면 내가 보낸거임.

            for (String friend : friendList) {
                if (search.equals(friend)) {
                    friendCheckDTO.setIsFriend(1);
                    break;
                }
            }
            friendCheckedList.add(friendCheckDTO);
        }
        return friendCheckedList;
    }

    @PostMapping("/sendRequest")
    @ResponseBody  // 응답을 JSON으로 반환하도록 설정
    public ResponseEntity<?> sendRequest(@RequestBody FriendDTO friendDTO, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        friendDTO.setRequesterId(userId);
        friendDTO.setStatus(0);
        boolean success = friendService.sendFriendRequest(friendDTO);
        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/cancelRequest")
    @ResponseBody  // 응답을 JSON으로 반환하도록 설정
    public ResponseEntity<?> cancelRequest(@RequestBody FriendDTO friendDTO, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        friendDTO.setRequesterId(userId);

        boolean success = friendService.cancelFriendRequest(friendDTO);
        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/acceptRequest")
    @ResponseBody  // 응답을 JSON으로 반환하도록 설정
    public ResponseEntity<?> acceptRequest(@RequestBody FriendDTO friendDTO, HttpServletRequest request) {
        log.info("friendDTO:{}", friendDTO);
        String userId = (String) request.getAttribute("userId");
        friendDTO.setFriendId(userId);

        boolean success = friendService.acceptFriendRequest(friendDTO);
        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping("/rejectRequest")
    @ResponseBody
    public ResponseEntity<?> rejectRequest(@RequestBody FriendDTO friendDTO, HttpServletRequest request) {
        log.info("friendDTO:{}", friendDTO);
        String userId = (String) request.getAttribute("userId");
        friendDTO.setFriendId(userId);

        boolean success = friendService.rejectFriendRequest(friendDTO);
        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("/received")
    public String received(Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<String> receivedList = friendService.receivedList(userId);
        log.info("receivedList: {}", receivedList);
        model.addAttribute("receivedList", receivedList);
        return "friend/received";
    }

    @GetMapping("/sent")
    public String sent(Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<String> sentList = friendService.sentList(userId);
        log.info("sentList: {}", sentList);
        model.addAttribute("sentList", sentList);
        return "friend/sent";
    }





}
