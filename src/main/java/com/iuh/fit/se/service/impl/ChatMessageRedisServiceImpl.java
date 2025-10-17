package com.iuh.fit.se.service.impl;

import com.iuh.fit.se.domain.dto.ChatMessageDTO;
import com.iuh.fit.se.service.ChatMessageRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatMessageRedisServiceImpl implements ChatMessageRedisService {

    private static final String CHAT_KEY_PREFIX = "chat:session:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveMessage(Long sessionId, ChatMessageDTO message) {
        String key = CHAT_KEY_PREFIX + sessionId;
        redisTemplate.opsForList().rightPush(key, message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ChatMessageDTO> getMessages(Long sessionId) {
        String key = CHAT_KEY_PREFIX + sessionId;
        return (List<ChatMessageDTO>)(List<?>) redisTemplate.opsForList().range(key, 0, -1);
    }
}