package com.rishit.SwiftChat.services;

import com.rishit.SwiftChat.dto.request.CreateUserRequest;
import com.rishit.SwiftChat.dto.response.UserResponse;
import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.repository.UserRepository;
import com.rishit.SwiftChat.services.impl.UserService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserService{

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PRESENCE_KEY = "user:presence:";


    @Override
    @CachePut(value = "users", key = "#result.id")
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

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public UserResponse updateUser(UUID userId, CreateUserRequest request){
        User user1 = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        user1.setUserName(request.getUserName());
        user1.setEmail(request.getEmail());
        return mapToUserResponse(user1);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void deleteUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUser(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    public void updateUserPresence(UUID userId, boolean isOnline){
        String key = PRESENCE_KEY + userId.toString();

        if (isOnline) {
            redisTemplate.opsForValue().set(key, true, 10, TimeUnit.MINUTES);
        } else {
            redisTemplate.delete(key);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("user not found"));

            user.setLastSeen(LocalDateTime.now());

            userRepository.save(user);
        }

    }

    public boolean isUserOnline(UUID userId) {
        Object val = redisTemplate.opsForValue().get(PRESENCE_KEY + userId.toString());
        return val != null && (boolean) val;
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
