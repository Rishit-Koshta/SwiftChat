package com.rishit.SwiftChat.config;


import com.rishit.SwiftChat.dto.event.PresenceEvent;
import com.rishit.SwiftChat.services.UserServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final UserServicesImpl userServices;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userIdStr = accessor.getFirstNativeHeader("userId");

        if (userIdStr != null) {
            UUID userId = UUID.fromString(userIdStr);
            accessor.getSessionAttributes().put("userId", userId); // Store in session

            // Update DB to Online
            userServices.updateUserPresence(userId, true);

            // Broadcast to everyone that this user is online
            messagingTemplate.convertAndSend("/topic/public", new PresenceEvent(userId, true));
        }

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        // Retrieve the userId we stored during connection
        UUID userId = (UUID) accessor.getSessionAttributes().get("userId");

        if (userId != null) {
            // Update DB to Offline and set lastSeen
            userServices.updateUserPresence(userId, false);

            // Broadcast to everyone that this user went offline
            messagingTemplate.convertAndSend("/topic/public", new PresenceEvent(userId, false));
        }
    }
}
