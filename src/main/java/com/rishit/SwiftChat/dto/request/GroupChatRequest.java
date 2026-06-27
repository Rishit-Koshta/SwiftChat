package com.rishit.SwiftChat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GroupChatRequest {

    private String groupName;
    private List<UUID> uuidList;
}
