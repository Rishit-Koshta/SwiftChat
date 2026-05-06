package com.rishit.SwiftChat.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue
    private UUID id;

    private Boolean isGroup;
    private String groupName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat")
    private List<ChatParticipants> participants;
}
