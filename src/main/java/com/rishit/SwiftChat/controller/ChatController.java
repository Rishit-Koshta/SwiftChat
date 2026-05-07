package com.rishit.SwiftChat.controller;


import com.rishit.SwiftChat.dto.request.GroupChatRequest;
import com.rishit.SwiftChat.dto.request.PrivateChatRequest;
import com.rishit.SwiftChat.dto.response.ChatResponse;
import com.rishit.SwiftChat.model.entity.Chat;
import com.rishit.SwiftChat.services.ChatServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatServicesImpl chatServices;


    @PostMapping("/private")
    public ResponseEntity<ChatResponse> createPrivateChat(
            @RequestBody PrivateChatRequest request) {

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(chatServices.createPrivateChat(request));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @PostMapping("/group")
    public ResponseEntity<ChatResponse> createGroupChat(
            GroupChatRequest request) {

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(chatServices.createGroupChat(request));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatResponse>> getUserChats(
            @PathVariable UUID userId) {

        try {

            return ResponseEntity.status(HttpStatus.OK).body(
                    chatServices.getUserChats(userId)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @PostMapping("/{chatId}/add/{userId}")
    public ResponseEntity<String> addUserToChat(
            @PathVariable UUID chatId,
            @PathVariable UUID userId) {

        try {

            chatServices.addUserToChat(chatId, userId);

            return ResponseEntity.ok("User added successfully");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
