package dev.nafplio.service.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatHistoryDto {
    private Long id;

    private String chatId;

    private String prompt;

    private String response;

    private LocalDateTime timestamp;
}
