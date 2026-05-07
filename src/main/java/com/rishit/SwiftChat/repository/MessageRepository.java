package com.rishit.SwiftChat.repository;

import com.rishit.SwiftChat.model.entity.Message;
import com.rishit.SwiftChat.model.enums.MessageStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatIdOrderByCreatedAtAsc(UUID chatId);
    List<Message> findAllByChatId(UUID chatId);

    @Modifying
    @Query("UPDATE Message m SET m.status = :targetStatus WHERE m.chat.id = :chatId AND m.sender.id != :userId AND m.status != :targetStatus")
    void markMessagesAsRead(
            @Param("chatId") UUID chatId,
            @Param("userId") UUID userId,
            @Param("targetStatus") MessageStatus targetStatus
    );

    Slice<Message> findByChatIdOrderByCreatedAtDesc(UUID chatId, Pageable pageable);
}
