package com.rishit.SwiftChat.controller;

import com.rishit.SwiftChat.dto.request.CreateUserRequest;
import com.rishit.SwiftChat.dto.response.UserResponse;
import com.rishit.SwiftChat.services.UserServicesImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServicesImpl userServices;

    @PostMapping("/save")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServices.saveUser(request));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(userServices.getUser(userId));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId){
        userServices.deleteUser(userId);
        return ResponseEntity.ok("User Deleted Successfully");
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId, @RequestBody CreateUserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userServices.updateUser(userId,request));
    }
}
