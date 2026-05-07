package com.rishit.SwiftChat.dto.event;


import com.rishit.SwiftChat.model.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {

    private UUID messageId;
    private UUID chatId;
    private MessageStatus status;

}
