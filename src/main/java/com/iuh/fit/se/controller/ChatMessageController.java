package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.ChatCustomerServiceDTO;
import com.iuh.fit.se.domain.dto.ChatMessageDTO;
import com.iuh.fit.se.domain.entity.ChatCustomerService;
import com.iuh.fit.se.domain.enums.ChatStatus;
import com.iuh.fit.se.repository.ChatCustomerServiceRepository;
import com.iuh.fit.se.service.ChatCustomerServiceService;
import com.iuh.fit.se.service.ChatMessageRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRedisService chatMessageRedisService;
    private final ChatCustomerServiceService chatCustomerServiceService;
    private final ChatCustomerServiceRepository chatCustomerServiceRepository;

    @MessageMapping("/sendMessage")
    public void handleSocketMessage(ChatMessageDTO message) {
        chatMessageRedisService.saveMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ChatMessageDTO message) {
        chatMessageRedisService.saveMessage(message);
        simpMessagingTemplate.convertAndSend("/topic/messages", message);
        return ResponseEntity.ok("Message sent");
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatMessageDTO>> getChatHistory(@PathVariable Long sessionId) {
        List<ChatMessageDTO> messages = chatMessageRedisService.getMessages(sessionId);
        return ResponseEntity.ok(messages);
    }

    // Trả về danh sách tất cả session chat
    @GetMapping("/sessions")
    public ResponseEntity<List<ChatCustomerServiceDTO>> getAllSessions() {
        return ResponseEntity.ok(chatCustomerServiceService.getAllChatSessions());
    }

    // Cho phép frontend gọi POST /sessions thay cho GET
    @PostMapping("/sessions")
    public ResponseEntity<List<ChatCustomerServiceDTO>> getAllSessionsViaPost() {
        return ResponseEntity.ok(chatCustomerServiceService.getAllChatSessions());
    }

    // Trả lời preflight OPTIONS cho /sessions (tránh 405)
    @RequestMapping(value = "/sessions", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> sessionsOptions() {
        return ResponseEntity.ok().build();
    }

    // Tạo phiên chat mới (đăng ký khách vãng lai)
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerSession(@RequestBody Map<String, String> payload) {
        String guestName = payload.getOrDefault("guestName", null);
        String guestEmail = payload.getOrDefault("guestEmail", null);

        ChatCustomerService session = new ChatCustomerService();
        session.setStartTime(LocalDateTime.now());
        session.setStatus(ChatStatus.ACTIVE);
        session.setGuestName(guestName);
        session.setGuestEmail(guestEmail);
        session.setIsGuestChat(true);

        chatCustomerServiceRepository.save(session);

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getSessionId());
        response.put("message", "Chat session created successfully");

        return ResponseEntity.ok(response);
    }

    // GET /register trả về 405 thay vì 404
    @GetMapping("/register")
    public ResponseEntity<String> registerGetNotAllowed() {
        return ResponseEntity.status(405).body("Method Not Allowed");
    }

    // Trả lời preflight OPTIONS cho /register
    @RequestMapping(value = "/register", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> registerOptions() {
        return ResponseEntity.ok().build();
    }
}