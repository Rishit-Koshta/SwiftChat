package com.rishit.SwiftChat.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SendMessageRequest {

    private UUID chatId;
    private UUID senderID;
    private String content;
}
