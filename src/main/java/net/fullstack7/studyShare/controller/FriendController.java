package net.fullstack7.studyShare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendCheckDTO;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.MemberDTO;
import net.fullstack7.studyShare.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/list")
    public String list(Model model) {
        String userId = "testuser2"; //세션아이디
        List<String> friendList = friendService.list(userId);
        log.info("friendList: {}", friendList);
        model.addAttribute("friendList", friendList);
        return "friend/list";
    }

//    @GetMapping("/find")
//    public String find(Model model) {
//        return "friend/find";
//    }

    @GetMapping("/searchUserById")
    @ResponseBody
    public List<FriendCheckDTO> searchUserById(@RequestParam String searchId) { // 여기에서 검색된 아이디가 있는지 없는지 여부를 확인하려면 ? 만약 null이 나오면 어떤 에러가 뜨는지?
        String userId = "testuser2"; //세션아이디
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
    public ResponseEntity<?> sendRequest(@RequestBody FriendDTO friendDTO) {
        String userId = "testuser2"; //세션아이디
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
    public ResponseEntity<?> cancelRequest(@RequestBody FriendDTO friendDTO) {
        String userId = "testuser2"; //세션아이디
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
    public ResponseEntity<?> acceptRequest(@RequestBody FriendDTO friendDTO) {
        log.info("friendDTO:{}", friendDTO);
        String userId = "testuser2"; //세션아이디
        friendDTO.setFriendId(userId);

        boolean success = friendService.acceptFriendRequest(friendDTO);
        if(success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }







}
