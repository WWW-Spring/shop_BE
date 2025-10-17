package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.ChatMessageDTO;
import com.iuh.fit.se.service.ChatMessageRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatMessageController {

    @Autowired
    private ChatMessageRedisService chatMessageRedisService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDTO sendMessage(ChatMessageDTO dto) {
        chatMessageRedisService.saveMessage(dto.getSessionId(), dto);
        return dto;
    }

    @GetMapping("/history/{sessionId}")
    public List<ChatMessageDTO> getHistory(@PathVariable Long sessionId) {
        return chatMessageRedisService.getMessages(sessionId);
    }
}
