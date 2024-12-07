package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}
