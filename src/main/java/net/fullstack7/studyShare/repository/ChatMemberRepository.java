package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.ChatMember;
import net.fullstack7.studyShare.domain.ChatRoom;
import net.fullstack7.studyShare.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Integer> {
    List<ChatMember> findByMember(Member member);

//    @Modifying
//    @Query("delete from ChatMember M where M.chatRoom.id=:roomId and M.member.userId=:userId")
    void deleteByChatRoomAndMember(ChatRoom chatRoom, Member member);

    boolean existsByMemberAndChatRoom(Member member, ChatRoom chatRoom);
    Optional<ChatMember> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);

    int countByChatRoom(ChatRoom chatRoom);
}
