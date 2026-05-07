package com.rishit.SwiftChat.model.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String userName;
    private String email;

    private LocalDateTime lastSeen;
    private boolean isOnline = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
