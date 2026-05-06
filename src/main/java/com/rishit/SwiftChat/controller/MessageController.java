package com.rishit.SwiftChat.controller;


import com.rishit.SwiftChat.model.entity.Message;
import com.rishit.SwiftChat.services.MessageService;
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

    private final MessageService messageService;

    @GetMapping("/getAllMessage/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable UUID chatId){
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessages(chatId));
    }

    @PostMapping("/sendMessage/{chatId}/{userId}")
    public ResponseEntity<Message> sendMessage(@PathVariable UUID chatId, @PathVariable UUID userId, @RequestParam String content){
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(chatId,userId,content));
    }
}
