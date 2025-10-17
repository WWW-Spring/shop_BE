package com.iuh.fit.se.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageResponseDTO {
    private String senderType;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
}