package com.rishit.SwiftChat.dto.event;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PresenceEvent {
    private UUID userId;
    private boolean isOnline;
}
