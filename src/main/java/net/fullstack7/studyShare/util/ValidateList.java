package net.fullstack7.studyShare.util;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidateList {
    public static boolean validateMyListParameters(int pageNo, String searchCategory,
                                                       String searchValue, HttpServletResponse response) {
        if (pageNo < 1) {
            JSFunc.alertBack("페이지 번호는 1 이상이어야 합니다.", response);
            return false;
        }

        if (searchCategory != null && !searchCategory.trim().isEmpty()
                && searchValue != null && !searchValue.trim().isEmpty()) {
            if (!("title".equals(searchCategory) || "content".equals(searchCategory))) {
                JSFunc.alertBack("유효하지 않은 검색 카테고리입니다: " + searchCategory, response);
                return false;
            }
        }
        return true;
    }

    public static boolean validateMyListParameters(int pageNo,
                                                       String searchCategory,
                                                       String searchValue,
                                                       LocalDateTime displayAt,
                                                       LocalDateTime displayEnd,
                                                       String sortType,
                                                       HttpServletResponse response) {
        if (!validateMyListParameters(pageNo, searchCategory, searchValue, response)) {
            return false;
        }
        if (displayAt != null && displayEnd != null && displayAt.isAfter(displayEnd)) {
            JSFunc.alertBack("시작일은 종료일 이전이어야 합니다.", response);
            return false;
        }
        if (sortType != null && !sortType.trim().isEmpty()
                && !("createdAt".equals(sortType) || "thumbUp".equals(sortType))) {
            JSFunc.alertBack("유효하지 않은 정렬 기준입니다: " + sortType, response);
            return false;
        }
        return true;
    }

}

