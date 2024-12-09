package net.fullstack7.studyShare.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface ChatMemberMapper {
    Set<String> getChatRoomMembers(Integer chatRoomId);
}
