package dev.nafplio.data.repository;

import dev.nafplio.data.entity.chat.ChatHistory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ChatHistoryRepository implements PanacheRepository<ChatHistory> {
    public List<ChatHistory> findByChatId(String chatId) {
        return find("chatId", Sort.by("timestamp"), chatId).list();
    }
}