package com.rishit.SwiftChat.controller;


import com.rishit.SwiftChat.model.entity.Chat;
import com.rishit.SwiftChat.services.ChatServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatServices chatServices;


    @PostMapping("/private")
    public ResponseEntity<Chat> createPrivateChat(
            @RequestParam UUID user1Id,
            @RequestParam UUID user2Id) {

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(chatServices.createPrivateChat(user1Id, user2Id));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupChat(
            @RequestParam String groupName,
            @RequestParam List<UUID> userIds) {

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(chatServices.createGroupChat(groupName, userIds));

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getUserChats(
            @PathVariable UUID userId) {

        try {

            return ResponseEntity.ok(
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
