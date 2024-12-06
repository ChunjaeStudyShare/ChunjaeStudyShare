package net.fullstack7.studyShare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.MemberDTO;
import net.fullstack7.studyShare.mapper.FriendMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class FriendServiceImpl implements FriendService {

    private final FriendMapper friendMapper;

    @Override
    public List<FriendDTO> list(String memberId) {
        return friendMapper.list(memberId);
    }

    @Override
    public List<String> searchUsersById(String userId) {
        return friendMapper.searchById(userId);
    }

}
