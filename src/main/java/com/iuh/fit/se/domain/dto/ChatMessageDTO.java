package com.iuh.fit.se.domain.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageDTO implements Serializable {
    Long sessionId;
    String sender;
    String senderName;
    String content;
    LocalDateTime timestamp = LocalDateTime.now();
}