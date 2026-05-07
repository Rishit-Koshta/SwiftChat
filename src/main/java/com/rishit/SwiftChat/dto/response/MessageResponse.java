package com.rishit.SwiftChat.dto.response;

import com.rishit.SwiftChat.model.enums.MessageStatus;
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
public class MessageResponse {

    private UUID messageId;
    private UUID chatId;
    private UUID userId;
    private String senderName;
    private LocalDateTime createdAt;
    private String content;

    private MessageStatus status;
}
