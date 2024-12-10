package net.fullstack7.studyShare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.studyShare.dto.apiResponse.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.fullstack7.studyShare.service.ThumbsUp.ThumbsUpService;
import net.fullstack7.studyShare.exception.CustomException;

@RestController
@RequestMapping("/thumbs-up")
@Log4j2
@RequiredArgsConstructor
public class ThumbsUpController {
    private final ThumbsUpService thumbsUpService;
    @PostMapping("/{postId}")
    public ResponseEntity<?> getThumbsUp(@PathVariable Integer postId, HttpServletRequest request) {
        try {
            String userId = (String) request.getAttribute("userId");
            boolean isThumbsUp = thumbsUpService.insertThumbsUp(postId, userId);
            if (isThumbsUp) {
                return ResponseEntity.ok(ApiResponse.success("좋아요 추가 성공", null));
            }else {
                thumbsUpService.deleteThumbsUp(postId, userId);
                return ResponseEntity.ok(ApiResponse.success("좋아요가 취소되었습니다.", null));
            }
        } catch (Exception e) {
            log.error("좋아요 추가 처리 중 오류가 발생했습니다: {}", postId, e);
            return ResponseEntity.internalServerError().body(ApiResponse.error("처리 중 오류가 발생했습니다."));
        }
    }

    // @DeleteMapping("/{postId}")
    // public ResponseEntity<?> deleteThumbsUp(@PathVariable Integer postId, HttpServletRequest request) {
    //     try {
    //         String userId = (String) request.getAttribute("userId");
    //         thumbsUpService.deleteThumbsUp(postId, userId);
    //         return ResponseEntity.ok(ApiResponse.success("좋아요 삭제 성공", null));
    //     } catch (NumberFormatException e) {
    //         log.error("게시글 ID가 숫자가 아닙니다: {}", postId, e);
    //         return ResponseEntity.badRequest().body(ApiResponse.error("게시글 ID는 숫자여야 합니다."));
    //     } catch (Exception e) {
    //         log.error("좋아요 삭제 처리 중 오류가 발생했습니다: {}", postId, e);
    //         return ResponseEntity.internalServerError().body(ApiResponse.error("처리 중 오류가 발생했습니다."));
    //     }
    // }
}

