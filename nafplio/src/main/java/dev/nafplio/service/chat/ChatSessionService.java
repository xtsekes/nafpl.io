package dev.nafplio.service.chat;

import dev.nafplio.data.entity.chat.ChatSession;
import dev.nafplio.data.repository.ChatSessionRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository) {
        this.chatSessionRepository = chatSessionRepository;
    }

    public List<ChatSession> getSessions() {
        return chatSessionRepository.findAll().list();
    }

    public List<ChatSession> getSessionsSorted() {
        return chatSessionRepository.findAll(Sort.by("updatedAt").descending()).list();
    }

    @Transactional
    public ChatSession createSession(String title) {
        ChatSession chatSession = new ChatSession();
        chatSession.setTitle(title != null ? title : "Untitled Chat");
        chatSessionRepository.persist(chatSession);

        return chatSession;
    }

    public void deleteSession(UUID id) {
        chatSessionRepository.deleteById(id);
    }
}
