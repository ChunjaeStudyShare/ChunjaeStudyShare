package net.fullstack7.studyShare.mapper;

import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.today.TodayDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TodayMapper {
    List<TodayDTO> todayList(LocalDateTime selectedDate);
}
