package com.rishit.SwiftChat.dto.event;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TypingEvent {

    private UUID userId;
    private UUID chatId;
    private String senderName;
    private boolean isTyping;

}
