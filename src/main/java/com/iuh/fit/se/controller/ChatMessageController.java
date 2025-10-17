package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.ChatMessageDTO;
import com.iuh.fit.se.service.ChatMessageRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRedisService chatMessageRedisService;

    @MessageMapping("/sendMessage")
    public void handleSocketMessage(ChatMessageDTO message) {
        System.out.println("📩 [WebSocket] Received message: " + message.getContent());

        chatMessageRedisService.saveMessage(message);

        simpMessagingTemplate.convertAndSend("/topic/messages", message);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessageDTO message) {
        System.out.println("📨 [HTTP] Received message: " + message.getContent());
        chatMessageRedisService.saveMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<Object>> getChatHistory(@PathVariable Long sessionId) {
        List<Object> messages = chatMessageRedisService.getMessages(sessionId);
        System.out.println("📜 [Redis] Loaded " + messages.size() + " messages for session " + sessionId);
        return ResponseEntity.ok(messages);
    }
}
