package com.rishit.SwiftChat.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GroupChatRequest {

    private String groupName;
    private List<UUID> uuidList;
}
