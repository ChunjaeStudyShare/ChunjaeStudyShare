package net.fullstack7.studyShare.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    public String mainPage(Model model, @RequestParam(required = false) LocalDateTime selectedDate, HttpServletRequest request) {
        if (selectedDate == null) {
            selectedDate = LocalDateTime.now(); // 초기 진입 시 현재 시간으로 설정함
        }
        String userId = (String) request.getAttribute("userId");
        List<TodayDTO> todayList = todayService.todayList(selectedDate, userId);
        List<TodayDTO> sharedPosts = todayService.sharedPosts(userId);

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int date = selectedDate.getDayOfMonth();
        log.info("sharedPosts 임니당: {}", sharedPosts);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("date", date);
        model.addAttribute("todayList", todayList);
        model.addAttribute("sharedPosts", sharedPosts);
        return "today/main";
    }

    @PostMapping("/list")
    @ResponseBody
    public ResponseEntity<List<TodayDTO>> list(@RequestBody Map<String, Integer> params, HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Integer year = params.get("year");
        Integer month = params.get("month");
        Integer date = params.get("date");

        LocalDateTime selectedDate = null;
        if (year != null && month != null && date != null) {
            LocalDateTime now = LocalDateTime.now();
            int currentHour = now.getHour();
            int currentMinute = now.getMinute();
            if(now.getYear() == year && now.getMonthValue() == month && now.getDayOfMonth() == date){
                selectedDate = LocalDateTime.of(year, month, date, currentHour, currentMinute);
            } else {
                selectedDate = LocalDateTime.of(year, month, date, 0, 0);
            }
        }
        List<TodayDTO> todayList = todayService.todayList(selectedDate, userId);
        return ResponseEntity.ok(todayList);
    }
}
