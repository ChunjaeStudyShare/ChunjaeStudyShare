package net.fullstack7.studyShare.util;

import net.fullstack7.studyShare.dto.admin.PageRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class JpaPageUtil {
    
    public static PageRequest getPageRequest(PageRequestDTO pageRequestDTO) {
        if (pageRequestDTO.getSortField() != null && !pageRequestDTO.getSortField().isEmpty() 
            && pageRequestDTO.getSortDirection() != null && !pageRequestDTO.getSortDirection().isEmpty()) {
            
            Sort.Direction direction = "desc".equalsIgnoreCase(pageRequestDTO.getSortDirection()) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
                
            return PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by(direction, pageRequestDTO.getSortField())
            );
        }
        
        return PageRequest.of(
            pageRequestDTO.getPage() - 1,
            pageRequestDTO.getSize(),
            Sort.by(Sort.Direction.ASC, "userId")
        );
    }
} 