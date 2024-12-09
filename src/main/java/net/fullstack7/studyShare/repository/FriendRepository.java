package net.fullstack7.studyShare.repository;

import net.fullstack7.studyShare.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
}
