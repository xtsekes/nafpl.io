package dev.nafplio.data.repository;

import dev.nafplio.data.entity.chat.ChatHistory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ChatHistoryRepository implements PanacheRepository<ChatHistory> {
    public List<ChatHistory> findBySessionId(UUID sessionId) {
        return find("chatSession.id", Sort.by("timestamp"), sessionId).list();
    }
}