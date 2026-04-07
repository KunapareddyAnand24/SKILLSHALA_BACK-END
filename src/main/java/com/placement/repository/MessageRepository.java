package com.placement.repository;

import com.placement.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.senderId = :u1 AND m.receiverId = :u2) OR (m.senderId = :u2 AND m.receiverId = :u1) ORDER BY m.sentAt ASC")
    List<Message> findConversation(Long u1, Long u2);

    List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);
    
    @Query("SELECT DISTINCT m.senderId FROM Message m WHERE m.receiverId = :receiverId")
    List<Long> findDistinctSenders(Long receiverId);
}
