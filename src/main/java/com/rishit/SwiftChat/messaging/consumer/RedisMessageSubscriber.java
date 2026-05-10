package com.rishit.SwiftChat.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishit.SwiftChat.messaging.payload.RedisMessagePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper; // Spring's JSON parser


    public void handleMessage(String message) {
        try {

            RedisMessagePayload redisPayload = objectMapper.readValue(message, RedisMessagePayload.class);
            messagingTemplate.convertAndSend(redisPayload.getDestination(), redisPayload.getPayload());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}