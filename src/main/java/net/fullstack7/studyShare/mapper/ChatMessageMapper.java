package net.fullstack7.studyShare.mapper;

import net.fullstack7.studyShare.dto.ChatMessageDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMessageMapper {
    Integer save(ChatMessageDTO chatMessage);
}
