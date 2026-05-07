package com.rishit.SwiftChat.controller;

import com.rishit.SwiftChat.dto.event.StatusUpdateRequest;
import com.rishit.SwiftChat.dto.event.TypingEvent;
import com.rishit.SwiftChat.dto.request.SendMessageRequest;
import com.rishit.SwiftChat.dto.response.MessageResponse;
import com.rishit.SwiftChat.services.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageServiceImpl messageService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload SendMessageRequest request){

        MessageResponse savedMessage = messageService.sendMessage(request);
        String destination = "/topic/chat"+ request.getChatId();

        messagingTemplate.convertAndSend(destination,savedMessage);
    }

    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload TypingEvent event){
        messagingTemplate.convertAndSend("/topic/chat"+event.getChatId()+"/typing",event);
    }

    @MessageMapping("/chat.updateStatus")
    public void updateMessageStatus(@Payload StatusUpdateRequest request) {

        //update the database (SENT to READ)
        MessageResponse updatedMessage = messageService.updateStatus(request.getMessageId(), request.getStatus());

        //broadcast the update back to the chat so the sender sees the double blue ticks!
        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.getChatId() + "/status",
                updatedMessage
        );
    }
}
