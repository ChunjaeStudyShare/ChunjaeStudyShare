package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.ChatMessage;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
    List<ChatMessage> findByChatRoomAndCreatedAtGreaterThanEqual(ChatRoom chatRoom, LocalDateTime createdAt);
    void deleteAllByChatRoom(ChatRoom chatRoom);
}
