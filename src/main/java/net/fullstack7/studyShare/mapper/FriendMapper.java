package net.fullstack7.studyShare.mapper;

import net.fullstack7.studyShare.dto.FriendDTO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface FriendMapper {
    List<FriendDTO> list(String memberId);
    List<String> searchById(String userId);
}
