package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import net.fullstack7.studyShare.domain.Member; 

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Friend f WHERE f.requester = :member OR f.friend = :member")
    void deleteByUserId(@Param("member") Member member);
    
    @Transactional
    void deleteByRequesterOrFriend(Member member, Member sameMember);
}
