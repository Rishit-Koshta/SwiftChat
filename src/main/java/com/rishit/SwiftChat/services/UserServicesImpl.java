package com.rishit.SwiftChat.services;

import com.rishit.SwiftChat.dto.request.CreateUserRequest;
import com.rishit.SwiftChat.dto.response.UserResponse;
import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.repository.UserRepository;
import com.rishit.SwiftChat.services.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserService{

    private final UserRepository userRepository;

    public UserResponse saveUser(CreateUserRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    public UserResponse updateUser(UUID userId, CreateUserRequest request){
        User user1 = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        user1.setUserName(request.getUserName());
        user1.setEmail(request.getEmail());
        return mapToUserResponse(user1);
    }

    public void deleteUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public UserResponse getUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    public void updateUserPresence(UUID userId, boolean isOnline){

        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found"));

        user.setOnline(isOnline);

        if(!isOnline){
            user.setLastSeen(LocalDateTime.now());
        }

        userRepository.save(user);
    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setUserName(user.getUserName());
        response.setId(user.getId());

        response.setOnline(user.isOnline());
        response.setLastSeen(user.getLastSeen());
        return response;
    }

}
