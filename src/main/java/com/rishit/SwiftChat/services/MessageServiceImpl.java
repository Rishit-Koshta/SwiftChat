package com.rishit.SwiftChat.services;


import com.rishit.SwiftChat.dto.request.SendMessageRequest;
import com.rishit.SwiftChat.dto.response.MessageResponse;
import com.rishit.SwiftChat.dto.response.PaginatedMessageResponse;
import com.rishit.SwiftChat.model.entity.Chat;
import com.rishit.SwiftChat.model.entity.Message;
import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.model.enums.MessageStatus;
import com.rishit.SwiftChat.repository.ChatParticipantsRepository;
import com.rishit.SwiftChat.repository.ChatRepository;
import com.rishit.SwiftChat.repository.MessageRepository;
import com.rishit.SwiftChat.repository.UserRepository;
import com.rishit.SwiftChat.services.impl.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatParticipantsRepository participantRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageResponse sendMessage(SendMessageRequest request) {

        Chat chat = chatRepository.findById(request.getChatId()).orElseThrow(()-> new RuntimeException("chat now found"));
        User sender = userRepository.findById(request.getSenderID()).orElseThrow(()->new RuntimeException("user not found"));

        boolean isParticipant = participantRepository
                .existsByChatIdAndUserId(request.getChatId(), request.getSenderID());

        if (!isParticipant) {
            throw new RuntimeException("User not part of this chat");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(request.getContent());

        Message savedMessage =  messageRepository.save(message);
        return mapToMessageResponse(savedMessage);
    }


    public List<MessageResponse> getMessages(UUID chatId) {
        List<Message> messages =  messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
        return messages.stream()
                .map(this::mapToMessageResponse)
                .toList();
    }

    public MessageResponse updateStatus(UUID messageId, MessageStatus newStatus){

        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("message not found"));

        message.setStatus(newStatus);

        Message savedMessage = messageRepository.save(message);

        return mapToMessageResponse(savedMessage);
    }

    @Transactional
    public void markChatMessagesAsRead(UUID chatId, UUID userId) {
        messageRepository.markMessagesAsRead(chatId, userId, MessageStatus.READ);
    }

    @Override
    public PaginatedMessageResponse getMessages(UUID chatId, int page, int size) {

        // 1. Create the pagination request
        Pageable pageable = PageRequest.of(page, size);

        // 2. Fetch the slice of entities from the database
        Slice<Message> messageSlice = messageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);

        // 3. Map the Entities to DTOs
        List<MessageResponse> messageDtos = messageSlice.getContent()
                .stream()
                .map(this::mapToMessageResponse)
                .toList();

        // 4. Return the paginated wrapper
        return new PaginatedMessageResponse(
                messageDtos,
                messageSlice.getNumber(),      // Current page number
                messageSlice.hasNext()         // Is there another page?
        );
    }

    private MessageResponse mapToMessageResponse(Message message){
        MessageResponse response = new MessageResponse();
        response.setMessageId(message.getId());
        response.setChatId(message.getChat().getId());
        response.setCreatedAt(message.getCreatedAt());
        response.setUserId(message.getSender().getId());
        response.setSenderName(message.getSender().getUserName());
        response.setContent(message.getContent());

        response.setStatus(message.getStatus());

        return response;
    }
}
