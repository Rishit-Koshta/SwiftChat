package com.rishit.SwiftChat.services.impl;

import com.rishit.SwiftChat.dto.request.SendMessageRequest;
import com.rishit.SwiftChat.dto.response.MessageResponse;
import com.rishit.SwiftChat.dto.response.PaginatedMessageResponse;
import com.rishit.SwiftChat.model.entity.Message;
import com.rishit.SwiftChat.model.enums.MessageStatus;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponse sendMessage(SendMessageRequest request);
    void markChatMessagesAsRead(UUID chatId, UUID userId);
    MessageResponse updateStatus(UUID messageId, MessageStatus newStatus);
    PaginatedMessageResponse getMessages(UUID chatId, int page, int size);
}
