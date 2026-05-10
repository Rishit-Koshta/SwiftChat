package com.rishit.SwiftChat.controller;

import com.rishit.SwiftChat.dto.request.StatusUpdateRequest;
import com.rishit.SwiftChat.dto.event.TypingEvent;
import com.rishit.SwiftChat.messaging.payload.RedisMessagePayload;
import com.rishit.SwiftChat.dto.request.SendMessageRequest;
import com.rishit.SwiftChat.dto.response.MessageResponse;
import com.rishit.SwiftChat.services.MessageServiceImpl;
import com.rishit.SwiftChat.services.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageServiceImpl messageService;
    private final ChannelTopic topic;
    private final RateLimiterService rateLimiterService;
    private final RedisTemplate<String, Object> redisTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload SendMessageRequest request){

        if (!rateLimiterService.isAllowed(request.getSenderID())) {
            System.out.println("User " + request.getSenderID() + " is spamming. Blocked.");
            return; // Abort saving the message!
        }

        MessageResponse savedMessage = messageService.sendMessage(request);
        String destination = "/topic/chat"+ request.getChatId();

        RedisMessagePayload payload = new RedisMessagePayload(destination, savedMessage);

        redisTemplate.convertAndSend(topic.getTopic(), payload);
    }

    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload TypingEvent event){
        String destination = "/topic/chat/" + event.getChatId() + "/typing";
        RedisMessagePayload payload = new RedisMessagePayload(destination, event);
        redisTemplate.convertAndSend(topic.getTopic(), payload);
    }

    @MessageMapping("/chat.updateStatus")
    public void updateMessageStatus(@Payload StatusUpdateRequest request) {

        //update the database (SENT to READ)
        MessageResponse updatedMessage = messageService.updateStatus(request.getMessageId(), request.getStatus());

        String destination = "/topic/chat/" + request.getChatId() + "/status";
        RedisMessagePayload payload = new RedisMessagePayload(destination, updatedMessage);

        redisTemplate.convertAndSend(topic.getTopic(), payload);
    }
}
