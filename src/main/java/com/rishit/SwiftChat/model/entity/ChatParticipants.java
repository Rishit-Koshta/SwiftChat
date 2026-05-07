package com.rishit.SwiftChat.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "chat_participants")
public class ChatParticipants {

    @Id
    private UUID id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime joinedAt;
}
