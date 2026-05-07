package com.rishit.SwiftChat.services.impl;

import com.rishit.SwiftChat.dto.request.CreateUserRequest;
import com.rishit.SwiftChat.dto.response.UserResponse;
import com.rishit.SwiftChat.model.entity.User;

import java.util.UUID;

public interface UserService {

    UserResponse saveUser(CreateUserRequest request);
    void deleteUser(UUID userId);
    UserResponse getUser(UUID userId);
    UserResponse updateUser(UUID userId, CreateUserRequest request);

}
