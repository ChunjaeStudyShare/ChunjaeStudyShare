package net.fullstack7.studyShare.util;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

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

}

