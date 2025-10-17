package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.ChatMessageDTO;
import com.iuh.fit.se.service.ChatMessageRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageRedisServiceImpl implements ChatMessageRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getKey(Long sessionId) {
        return "chat:session:" + sessionId;
    }

    // ✅ Lưu tin nhắn vào Redis list
    @Override
    public void saveMessage(ChatMessageDTO message) {
        try {
            redisTemplate.opsForList().rightPush(getKey(message.getSessionId()), message);
            System.out.println("💾 [Redis] Saved message for session " + message.getSessionId());
        } catch (Exception e) {
            System.err.println("❌ [Redis] Failed to save message: " + e.getMessage());
        }
    }

    // ✅ Lấy toàn bộ lịch sử chat theo sessionId
    @Override
    public List<Object> getMessages(Long sessionId) {
        try {
            return redisTemplate.opsForList().range(getKey(sessionId), 0, -1);
        } catch (Exception e) {
            System.err.println("❌ [Redis] Failed to get messages: " + e.getMessage());
            return List.of();
        }
    }
}
