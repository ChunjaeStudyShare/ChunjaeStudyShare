package net.fullstack7.studyShare.service;

import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.post.PostDTO;
import net.fullstack7.studyShare.dto.today.TodayDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TodayService {
    List<TodayDTO> todayList(LocalDateTime selectedDate, String userId);
    List<TodayDTO> sharedPosts(String userId);
}
