package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.GuestRequestDTO;
import com.iuh.fit.se.domain.entity.ChatSession;
import com.iuh.fit.se.repository.ChatSessionRepository;
import com.iuh.fit.se.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatSessionRepository chatRepo;
    private final MessageRepository messageRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<?> startChat(@RequestBody GuestRequestDTO req) {
        ChatSession chat = new ChatSession();
        chat.setGuestName(req.getFullName());
        chat.setGuestPhone(req.getPhoneNumber());
        chat.setSessionCode("CHAT_" + System.currentTimeMillis());
        chatRepo.save(chat);

        messagingTemplate.convertAndSend("/topic/chat/global", Map.of(
                "type", "NEW_SESSION",
                "sessionCode", chat.getSessionCode(),
                "guestName", chat.getGuestName(),
                "guestPhone", chat.getGuestPhone()
        ));

        return ResponseEntity.ok(Map.of(
                "chatId", chat.getId(),
                "sessionCode", chat.getSessionCode(),
                "message", "Tạo phiên chat thành công"
        ));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveChats() {
        return ResponseEntity.ok(chatRepo.findByStatus("ACTIVE"));
    }

    @GetMapping("/{sessionId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long sessionId) {
        var session = chatRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        var messages = messageRepo.findBySessionOrderBySentAtAsc(session);
        return ResponseEntity.ok(messages);
    }
}
