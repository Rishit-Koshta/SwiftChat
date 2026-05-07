package com.rishit.SwiftChat.dto.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypingEvent {

    private UUID userId;
    private UUID chatId;
    private String senderName;
    private boolean isTyping;

}
