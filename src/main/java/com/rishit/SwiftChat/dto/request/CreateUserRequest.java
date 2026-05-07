package com.rishit.SwiftChat.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String userName;
    private String email;
}
