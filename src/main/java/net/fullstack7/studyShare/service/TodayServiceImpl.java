package net.fullstack7.studyShare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.post.PostDTO;
import net.fullstack7.studyShare.dto.post.PostShareDTO;
import net.fullstack7.studyShare.dto.today.TodayDTO;
import net.fullstack7.studyShare.mapper.FriendMapper;
import net.fullstack7.studyShare.mapper.TodayMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TodayServiceImpl implements TodayService {

    private final TodayMapper todayMapper;

    @Override
    public List<TodayDTO> todayList(LocalDateTime selectedDate, String userId) {
        List<TodayDTO> todayList = todayMapper.todayList(selectedDate, userId);
        for(TodayDTO todayDTO : todayList) {
            int postId = todayDTO.getId();
            List<PostShareDTO> sharedList = todayMapper.sharedIdList(postId);
            todayDTO.setSharedList(sharedList);
        }
        return todayList;
    }

    @Override
    public List<TodayDTO> sharedPosts(String userId) {
        return todayMapper.sharedPosts(userId);
    }
}
