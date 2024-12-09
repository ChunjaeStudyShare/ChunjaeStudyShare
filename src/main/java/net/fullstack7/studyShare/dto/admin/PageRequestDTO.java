package net.fullstack7.studyShare.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class PageRequestDTO {
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다")
    private int page = 1;

    @Min(value = 5, message = "페이지 크기는 5 이상이어야 합니다")
    @Max(value = 100, message = "페이지 크기는 100을 초과할 수 없습니다")
    private int size = 10;

    @Pattern(regexp = "^(userId|name|email|status|)$", message = "허용되지 않은 정렬 필드입니다")
    private String sortField;

    @Pattern(regexp = "^(asc|desc|)$", message = "정렬 방향은 asc 또는 desc만 가능합니다")
    private String sortDirection;

    // 검색 조건
    @Pattern(regexp = "^(userId|name|email|status|all|)$", message = "허용되지 않은 검색 필드입니다")
    private String searchField;

    // 검색어
    @Pattern(regexp = "^[가-힣a-zA-Z0-9@._-]*$", message = "검색어에 특수문자를 포함할 수 없습니다")
    private String searchKeyword;

    // 기본 생성자는 기본값 설정
    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    // 검색 조건이 있는지 확인하는 메서드
    public boolean hasSearch() {
        return searchField != null && searchKeyword != null 
               && !searchField.isEmpty() && !searchKeyword.isEmpty();
    }
}