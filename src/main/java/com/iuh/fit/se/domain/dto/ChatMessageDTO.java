package com.iuh.fit.se.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    private String sender;       // "customer" hoặc "staff"
    private String senderName;
    private String phone;
    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();
    private Long chatId;
}