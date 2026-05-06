package com.rishit.SwiftChat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import com.rishit.SwiftChat.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, UUID>{

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findByUserName(String userName);
}
