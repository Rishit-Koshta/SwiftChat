package com.rishit.SwiftChat.controller;


import com.rishit.SwiftChat.document.MessageDocument;
import com.rishit.SwiftChat.dto.request.SendMessageRequest;
import com.rishit.SwiftChat.dto.response.MessageResponse;
import com.rishit.SwiftChat.dto.response.PaginatedMessageResponse;
import com.rishit.SwiftChat.model.entity.Message;
import com.rishit.SwiftChat.repository.MessageSearchRepository;
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
    private final MessageSearchRepository searchRepository;
    @PostMapping("/sendMessage/{chatId}/{userId}")
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody SendMessageRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.sendMessage(request));
    }

    @PutMapping("/readAll/{chatId}/user/{userId}")
    public ResponseEntity<String> markAllMessageAsRead(@PathVariable UUID chatId, @PathVariable UUID userID){
        messageService.markChatMessagesAsRead(chatId,userID);
        return ResponseEntity.ok("All messages are read");
    }

    @GetMapping("/getAllMessage/{chatId}")
    public ResponseEntity<PaginatedMessageResponse> getMessages(
            @PathVariable UUID chatId,
            @RequestParam(defaultValue = "0") int page,    // Default to page 0
            @RequestParam(defaultValue = "20") int size) { // Default to 20 messages per page

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messageService.getMessages(chatId, page, size));
    }

    @GetMapping("/search/{chatId}")
    public ResponseEntity<List<MessageDocument>> searchMessages(
            @PathVariable UUID chatId,
            @RequestParam String keyword) {

        // This hits Elasticsearch, NOT MySQL!
        // It returns results instantly even if there are 10 million messages.
        List<MessageDocument> searchResults = searchRepository.findByChatIdAndContentMatches(chatId, keyword);

        return ResponseEntity.ok(searchResults);
    }
}
