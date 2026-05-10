package com.rishit.SwiftChat.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Configuration
    private static final int MAX_REQUESTS = 5;
    private static final int WINDOW_IN_SECONDS = 3;

    public boolean isAllowed(UUID userId) {
        String key = "rate_limit:user:" + userId;

        // Increment the user's count in Redis
        Long currentCount = redisTemplate.opsForValue().increment(key);

        if (currentCount != null) {
            if (currentCount == 1) {
                // If it's their first message in this window, start the 3-second timer
                redisTemplate.expire(key, Duration.ofSeconds(WINDOW_IN_SECONDS));
            }

            // If they exceeded 5 messages, block them
            if (currentCount > MAX_REQUESTS) {
                return false;
            }
        }

        return true;
    }
}