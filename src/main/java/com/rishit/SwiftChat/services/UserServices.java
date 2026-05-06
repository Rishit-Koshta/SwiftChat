package com.rishit.SwiftChat.services;

import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServices {

    private final UserRepository userRepository;

    public User saveUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("User already exists");
        }
        user.setUserName(user.getUserName());
        user.setEmail(user.getEmail());
        return userRepository.save(user);
    }

    public User updateUser(UUID userId, User user){
        User user1 = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        user1.setUserName(user.getUserName());
        user1.setEmail(user.getEmail());
        return userRepository.save(user1);
    }

    public void deleteUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public User getUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }


}
