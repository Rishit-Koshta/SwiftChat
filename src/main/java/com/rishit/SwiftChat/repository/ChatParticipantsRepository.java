package com.rishit.SwiftChat.repository;

import com.rishit.SwiftChat.model.entity.ChatParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatParticipantsRepository extends JpaRepository<ChatParticipants, UUID> {

    List<ChatParticipants> findByUserId(UUID userId);
    boolean existsByChatIdAndUserId(UUID chatId, UUID userId);
}