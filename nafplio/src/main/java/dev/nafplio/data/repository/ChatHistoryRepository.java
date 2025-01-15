package dev.nafplio.data.repository;

import dev.nafplio.data.entity.ChatHistory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ChatHistoryRepository implements PanacheRepository<ChatHistory> {
    public List<ChatHistory> findByChatId(UUID chatId) {
        return find("chatSession.id", chatId).list();
    }
}