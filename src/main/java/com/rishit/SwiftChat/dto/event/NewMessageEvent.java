package com.rishit.SwiftChat.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewMessageEvent {
    private UUID messageId;
    private UUID chatId;
    private UUID senderId;
    private String senderName;
    private String content;
    private LocalDateTime createdAt;
}