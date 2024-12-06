package net.fullstack7.studyShare.service;

import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.MemberDTO;

import java.util.List;

public interface FriendService {
    List<FriendDTO> list(String memberId);
    public List<String> searchUsersById(String userId);
}
