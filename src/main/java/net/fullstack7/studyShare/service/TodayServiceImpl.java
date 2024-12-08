package net.fullstack7.studyShare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendDTO;
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
    public List<TodayDTO> todayList(LocalDateTime selectedDate) {
        return todayMapper.todayList(selectedDate);
    }
}
