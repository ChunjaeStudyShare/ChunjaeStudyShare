package net.fullstack7.studyShare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.FriendCheckDTO;
import net.fullstack7.studyShare.dto.FriendDTO;
import net.fullstack7.studyShare.dto.today.TodayDTO;
import net.fullstack7.studyShare.service.FriendService;
import net.fullstack7.studyShare.service.TodayService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/today")
public class TodayController {
    private final TodayService todayService;

    @GetMapping("/main")
    public String mainPage(Model model, @RequestParam(required = false) LocalDateTime selectedDate) {
        if (selectedDate == null) {
            selectedDate = LocalDateTime.now(); // 초기 진입 시 현재 시간으로 설정
        }
        List<TodayDTO> todayList = todayService.todayList(selectedDate);
        log.info("todayList: {}", todayList);
        model.addAttribute("todayList", todayList);
        return "today/main";
    }

    @PostMapping("/list")
    public ResponseEntity<List<TodayDTO>> list(@RequestBody Map<String, Integer> params) {
        Integer year = params.get("year");
        Integer month = params.get("month");
        Integer date = params.get("date");

        LocalDateTime selectedDate = null;
        if (year != null && month != null && date != null) {
            selectedDate = LocalDateTime.of(year, month, date, 0, 0);
        }
        List<TodayDTO> todayList = todayService.todayList(selectedDate);
        log.info("todayList: {}", todayList);
        return ResponseEntity.ok(todayList);
    }
}
