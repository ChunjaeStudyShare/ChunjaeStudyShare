package net.fullstack7.studyShare.mapper;

import net.fullstack7.studyShare.domain.Post;
import net.fullstack7.studyShare.dto.post.PostViewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    int totalCnt(@Param("searchCategory") String searchCategory, @Param("searchValue") String searchValue, @Param("userId") String userId,
                 @Param("sortType") String sortType, @Param("displayAt") LocalDateTime displayAt, @Param("displayEnd") LocalDateTime displayEnd);
    List<Post> selectAllPost(Map<String, Object> map);

    List<Post> selectMyShare(Map<String, Object> map);

    PostViewDTO findPostWithFile(@Param("id") String id);

    boolean checkWriter(@Param("id") int id, @Param("userId") String userId);

    boolean deletePost(int id);
    boolean deleteShare(int id);
    Integer hasShare(int id);
}
