package com.rishit.SwiftChat.messaging.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedisMessagePayload {
    private String destination; // e.g., "/topic/chat/123-456"
    private Object payload;     // Your MessageResponse, TypingEvent, etc.
}