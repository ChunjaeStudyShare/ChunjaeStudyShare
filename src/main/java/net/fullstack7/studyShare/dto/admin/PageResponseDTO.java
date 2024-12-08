package net.fullstack7.studyShare.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<T> {
    private List<T> content;
    private int currentPage;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private String sortField;
    private String sortDirection;
    private String searchField;
    private String searchKeyword;

    @Builder
    public PageResponseDTO(List<T> content, int currentPage, int size, long totalElements, 
                         String sortField, String sortDirection,
                         String searchField, String searchKeyword) {
        this.content = content;
        this.currentPage = currentPage;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.first = currentPage == 1;
        this.last = currentPage == totalPages;
        this.sortField = sortField;
        this.sortDirection = sortDirection;
        this.searchField = searchField;
        this.searchKeyword = searchKeyword;
    }
}