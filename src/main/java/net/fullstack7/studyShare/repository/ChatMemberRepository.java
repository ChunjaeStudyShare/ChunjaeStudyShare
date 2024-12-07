package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {
    List<ChatMember> findByMember(Member member);

    @Query("delete from ChatMember M where M.chatRoom.id=:roomId and M.member.userId=:userId")
    void deleteMember(int roomId, String userId);
}
