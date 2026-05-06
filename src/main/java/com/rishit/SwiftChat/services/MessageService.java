package com.rishit.SwiftChat.services;


import com.rishit.SwiftChat.model.entity.Chat;
import com.rishit.SwiftChat.model.entity.Message;
import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.repository.ChatParticipantsRepository;
import com.rishit.SwiftChat.repository.ChatRepository;
import com.rishit.SwiftChat.repository.MessageRepository;
import com.rishit.SwiftChat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatParticipantsRepository participantRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public Message sendMessage(UUID chatId, UUID senderId, String content) {

        Chat chat = chatRepository.findById(chatId).orElseThrow(()-> new RuntimeException("chat now found"));
        User sender = userRepository.findById(senderId).orElseThrow(()->new RuntimeException("user not found"));

        boolean isParticipant = participantRepository
                .existsByChatIdAndUserId(chatId, senderId);

        if (!isParticipant) {
            throw new RuntimeException("User not part of this chat");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);

        return messageRepository.save(message);
    }


    public List<Message> getMessages(UUID chatId) {
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
    }
}
