package com.rishit.SwiftChat.services.impl;

import com.rishit.SwiftChat.dto.request.GroupChatRequest;
import com.rishit.SwiftChat.dto.request.PrivateChatRequest;
import com.rishit.SwiftChat.dto.response.ChatResponse;
import com.rishit.SwiftChat.model.entity.Chat;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatResponse createPrivateChat(PrivateChatRequest request);
    ChatResponse createGroupChat(GroupChatRequest request);
    List<ChatResponse> getUserChats(UUID userId);
    void addUserToChat(UUID chatId, UUID userId);
}
