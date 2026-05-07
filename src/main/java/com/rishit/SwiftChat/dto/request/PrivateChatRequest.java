package com.rishit.SwiftChat.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PrivateChatRequest {
    private UUID user1Id;
    private UUID user2Id;
}
