package com.iuh.fit.se.controller;

import com.iuh.fit.se.domain.dto.ChatMessageDTO;
import com.iuh.fit.se.service.ChatCustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @Autowired
    private ChatCustomerServiceService chatService;

    @MessageMapping("/sendMessage")   // Client gửi lên /app/sendMessage
    @SendTo("/topic/messages")        // Tự động gửi lại tất cả client đang subscribe
    public ChatMessageDTO sendMessage(ChatMessageDTO message) {
        chatService.saveMessage(message);
        return message;
    }
}
