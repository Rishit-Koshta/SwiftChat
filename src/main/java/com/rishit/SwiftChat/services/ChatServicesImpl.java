package com.rishit.SwiftChat.services;

import com.rishit.SwiftChat.dto.request.GroupChatRequest;
import com.rishit.SwiftChat.dto.request.PrivateChatRequest;
import com.rishit.SwiftChat.dto.response.ChatResponse;
import com.rishit.SwiftChat.model.entity.Chat;
import com.rishit.SwiftChat.model.entity.ChatParticipants;
import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.repository.ChatParticipantsRepository;
import com.rishit.SwiftChat.repository.ChatRepository;
import com.rishit.SwiftChat.repository.UserRepository;
import com.rishit.SwiftChat.services.impl.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ChatServicesImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatParticipantsRepository chatParticipantsRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ChatResponse createPrivateChat(PrivateChatRequest request){

        String lockKey = "lock:chat:" + request.getUser1Id() + ":" + request.getUser2Id();

        // Attempt to get a lock for 5 seconds
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", 5, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(acquired)) {
            throw new RuntimeException("Chat creation already in progress...");
        }

        try {

            Chat chat = new Chat();
            chat.setIsGroup(false);

            chatRepository.save(chat);

            List<UUID> uuidList = List.of(request.getUser1Id(),request.getUser2Id());
            List<User> users = userRepository.findAllById(uuidList);

            if(users.size()!=2){
                throw new RuntimeException("user not found");
            }

            chatParticipantsRepository.save(new ChatParticipants(chat,users.get(0)));
            chatParticipantsRepository.save(new ChatParticipants(chat,users.get(0)));

            return mapToChatResponse(chat);

        } finally {
            redisTemplate.delete(lockKey); // Always release the lock!
        }

    }

    public ChatResponse createGroupChat(GroupChatRequest request){

        Chat chat = new Chat();
        chat.setIsGroup(true);
        chat.setGroupName(request.getGroupName());

        chatRepository.save(chat);

        List<User> users = userRepository.findAllById(request.getUuidList());

        for(User user : users){
            chatParticipantsRepository.save(new ChatParticipants(chat,user));
        }

        return mapToChatResponse(chat);

    }

    public List<ChatResponse> getUserChats(UUID userId){

        List<ChatParticipants> participants =
                chatParticipantsRepository.findByUserId(userId);

        List<UUID> chatIds = participants.stream()
                .map(cp -> cp.getChat().getId())
                .toList();

        List<Chat> chatList =  chatRepository.findAllById(chatIds);

        return chatList.stream()
                .map(this::mapToChatResponse)
                .toList();
    }

    public void addUserToChat(UUID chatId, UUID userId){

        Chat chat = chatRepository.findById(chatId).orElseThrow(()->new RuntimeException("chat not found"));
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found"));

        boolean exists = chatParticipantsRepository.existsByChatIdAndUserId(chatId,userId);
        if(exists){
            throw new RuntimeException("User already exists in chat");
        }

        ChatParticipants chatParticipants = new ChatParticipants();
        chatParticipants.setId(UUID.randomUUID());
        chatParticipants.setChat(chat);
        chatParticipants.setUser(user);
        chatParticipants.setJoinedAt(LocalDateTime.now());

        chatParticipantsRepository.save(chatParticipants);

    }


    private ChatResponse mapToChatResponse(Chat chat){
        ChatResponse response = new ChatResponse();
        response.setId(chat.getId());
        response.setGroupName(chat.getGroupName());
        response.setIsGroup(chat.getIsGroup());
        response.setCreatedAt(chat.getCreatedAt());

        return response;
    }


}
