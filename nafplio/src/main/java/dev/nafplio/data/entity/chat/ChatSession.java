package dev.nafplio.data.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class ChatSession {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }
}
