package dev.nafplio.service.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatSessionDto {
    private String id;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
