package com.rishit.SwiftChat.controller;

import com.rishit.SwiftChat.model.entity.User;
import com.rishit.SwiftChat.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserServices userServices;

    @PostMapping("/save")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServices.saveUser(user));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<User> getUser(@PathVariable UUID userId){
        return ResponseEntity.status(HttpStatus.OK).body(userServices.getUser(userId));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId){
        userServices.deleteUser(userId);
        return ResponseEntity.ok("User Deleted Successfully");
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId, @RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userServices.updateUser(userId,user));
    }
}
