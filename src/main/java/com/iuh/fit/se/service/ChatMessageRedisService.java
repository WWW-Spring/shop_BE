package com.iuh.fit.se.service;

import com.iuh.fit.se.domain.dto.ChatMessageDTO;
import java.util.List;

public interface ChatMessageRedisService {
    void saveMessage(ChatMessageDTO message);
    List<Object> getMessages(Long sessionId);
}
