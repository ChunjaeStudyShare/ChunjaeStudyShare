package net.fullstack7.studyShare.util;

import net.fullstack7.studyShare.dto.admin.PageResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class PageUrlBuilder {
    
    public String buildUrl(String baseUrl, PageResponseDTO pageResponse, Integer targetPage) {
        return UriComponentsBuilder.fromPath(baseUrl)
                .queryParam("page", targetPage)
                .queryParam("size", pageResponse.getSize())
                .queryParam("sortField", pageResponse.getSortField())
                .queryParam("sortDirection", pageResponse.getSortDirection())
                .queryParam("searchField", pageResponse.getSearchField())
                .queryParam("searchKeyword", pageResponse.getSearchKeyword())
                .build()
                .toString();
    }

    public String buildSortUrl(String baseUrl, PageResponseDTO pageResponse, 
                             String sortField, String currentSortDirection) {
        String newSortDirection = sortField.equals(pageResponse.getSortField()) && 
                                "asc".equals(currentSortDirection) ? "desc" : "asc";
        
        return UriComponentsBuilder.fromPath(baseUrl)
                .queryParam("page", pageResponse.getCurrentPage())
                .queryParam("size", pageResponse.getSize())
                .queryParam("sortField", sortField)
                .queryParam("sortDirection", newSortDirection)
                .queryParam("searchField", pageResponse.getSearchField())
                .queryParam("searchKeyword", pageResponse.getSearchKeyword())
                .build()
                .toString();
    }
} 