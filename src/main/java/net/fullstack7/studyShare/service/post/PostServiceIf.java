package net.fullstack7.studyShare.service.post;

import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.dto.post.*;
import net.fullstack7.studyShare.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostServiceIf {
    boolean regist(PostRegistDTO postRegistDTO, String memberId) throws IOException;

    int totalCnt(String searchCategory, String searchValue, String userId, String sortType, LocalDateTime displayAt, LocalDateTime displayEnd);

    List<PostDTO> selectAllPost(int pageNo, int pageSize, String searchCategory, String searchValue,
                                String userId, String sortType, LocalDateTime displayAt, LocalDateTime displayEnd);

    List<PostGetInfoDTO> selectMyShare(int pageNo, int pageSize, String searchCategory, String searchValue,
                                       String userId, String sortType, LocalDateTime displayAt, LocalDateTime displayEnd);

    PostViewDTO findPostWithFile(String id);

    boolean checkWriter(int id, String userId);

    PostRegistDTO modifyPost(PostRegistDTO dto, String userId);

    boolean delete(int id);
}