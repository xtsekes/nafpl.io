package dev.nafplio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatHistory {
    private String id;

    private String chatId;

    private String prompt;

    private String response;

    private LocalDateTime timestamp;
}
