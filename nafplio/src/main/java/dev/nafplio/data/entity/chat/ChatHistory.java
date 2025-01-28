package dev.nafplio.data.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_history")
@Getter
@Setter
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chatId;

    @Column(nullable = false)
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
