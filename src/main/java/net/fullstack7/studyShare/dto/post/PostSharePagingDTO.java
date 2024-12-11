package net.fullstack7.studyShare.dto.post;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import lombok.AllArgsConstructor;

@Builder
@AllArgsConstructor
@Log4j2
@Data
public class PostSharePagingDTO {
    @Positive
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private int pageNo = 1;

    @Positive
    @Min(value = 5, message = "페이지 크기는 5 이상이어야 합니다.")
    @Max(value = 100, message = "페이지 크기는 100을 초과할 수 없습니다.")
    private int pageSize = 10;

    @Pattern(regexp = "^(title|content|userId|)$", message = "허용되지 않은 검색 카테고리입니다")
    private String searchCategory;

    @Size(max = 101, message = "검색 값은 100자를 초과할 수 없습니다.")
    private String searchValue;

    @Pattern(regexp = "^(share|receiveShare)$", message = "정렬 기준은 내가 한 공유 또는 내가 받은 공유이어야 합니다.")
    private String sortType = "share";

    private LocalDateTime displayAt;
    private LocalDateTime displayEnd;

    private String userId;

    @Positive
    @Min(value = 1, message = "블럭 크기는 1 이상이어야 합니다.")
    private int blockSize = 5;

    public PostSharePagingDTO() {
        this.pageNo = 1;
        this.pageSize = 10;
        this.blockSize = 5;
        this.sortType = "share";
    }

    @AssertTrue(message = "시작일은 종료일 이전이어야 합니다.")
    public boolean isDisplayAtBeforeDisplayEnd() {
        if (displayAt == null || displayEnd == null) {
            return true;
        }
        return displayAt.isBefore(displayEnd);
    }
}