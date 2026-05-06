package com.rishit.SwiftChat.services;

import com.rishit.SwiftChat.model.entity.Chat;
import com.rishit.SwiftChat.model.entity.ChatParticipants;
import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.repository.ChatParticipantsRepository;
import com.rishit.SwiftChat.repository.ChatRepository;
import com.rishit.SwiftChat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ChatServices {

    private final ChatRepository chatRepository;
    private final ChatParticipantsRepository chatParticipantsRepository;
    private final UserRepository userRepository;

    public Chat createPrivateChat(UUID user1Id, UUID user2Id){

        Chat chat = new Chat();
        chat.setIsGroup(false);

        chatRepository.save(chat);

        List<UUID> uuidList = List.of(user1Id,user2Id);
        List<User> users = userRepository.findAllById(uuidList);

        if(users.size()!=2){
            throw new RuntimeException("user not found");
        }

        chatParticipantsRepository.save(new ChatParticipants(UUID.randomUUID(),chat, users.get(0),LocalDateTime.now()));
        chatParticipantsRepository.save(new ChatParticipants(UUID.randomUUID(),chat, users.get(1),LocalDateTime.now()));

        return chat;
    }

    public Chat createGroupChat(String groupName, List<UUID>uuidList ){

        Chat chat = new Chat();
        chat.setIsGroup(true);
        chat.setGroupName(groupName);

        chatRepository.save(chat);

        List<User> users = userRepository.findAllById(uuidList);

        for(User user : users){
            chatParticipantsRepository.save(new ChatParticipants(UUID.randomUUID(), chat, user, LocalDateTime.now()));
        }

        return chat;

    }

    public List<Chat> getUserChats(UUID userId){

        List<ChatParticipants> participants =
                chatParticipantsRepository.findByUserId(userId);

        List<UUID> chatIds = participants.stream()
                .map(cp -> cp.getChat().getId())
                .toList();

        return chatRepository.findAllById(chatIds);
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


}
