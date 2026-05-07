package com.rishit.SwiftChat.controller;


import com.rishit.SwiftChat.dto.request.SendMessageRequest;
import com.rishit.SwiftChat.dto.response.MessageResponse;
import com.rishit.SwiftChat.model.entity.Message;
import com.rishit.SwiftChat.services.MessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageServiceImpl messageService;

    @GetMapping("/getAllMessage/{chatId}")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable UUID chatId){
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessages(chatId));
    }

    @PostMapping("/sendMessage/{chatId}/{userId}")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody SendMessageRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(request));
    }

    @PutMapping("/readAll/{chatId}/user/{userId}")
    public ResponseEntity<String> markAllMessageAsRead(@PathVariable UUID chatId, @PathVariable UUID userID){
        messageService.markChatMessagesAsRead(chatId,userID);
        return ResponseEntity.ok("All messages are read");
    }
}
