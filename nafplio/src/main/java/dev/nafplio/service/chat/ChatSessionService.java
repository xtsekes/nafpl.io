package dev.nafplio.service.chat;

import dev.nafplio.data.entity.chat.ChatSession;
import dev.nafplio.data.repository.ChatSessionRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ChatSessionService {
    private final ChatSessionRepository chatSessionRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository) {
        this.chatSessionRepository = chatSessionRepository;
    }

    public List<ChatSessionDto> getSessions() {
        return chatSessionRepository.findAll()
                .list()
                .stream()
                .map(ChatSessionService::map)
                .toList();
    }

    public List<ChatSessionDto> getSessionsSorted() {
        return chatSessionRepository
                .findAll(Sort.by("updatedAt").descending())
                .list()
                .stream()
                .map(ChatSessionService::map)
                .toList();
    }

    @Transactional
    public ChatSessionDto getSession(String id) {
        return map(chatSessionRepository.findById(id));
    }

    @Transactional
    public ChatSessionDto createSession(String title) {
        var chatSession = new ChatSession();
        chatSession.setId(UUID.randomUUID().toString());
        chatSession.setTitle(title != null ? title : "Untitled Chat");
        chatSession.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        chatSession.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        chatSessionRepository.persist(chatSession);

        return map(chatSession);
    }

    public void deleteSession(String id) {
        chatSessionRepository.deleteById(id);
    }

    private static ChatSessionDto map(ChatSession chatSession) {
        var chatSessionDto = new ChatSessionDto();

        chatSessionDto.setId(chatSession.getId());
        chatSessionDto.setTitle(chatSession.getTitle());
        chatSessionDto.setCreatedAt(chatSession.getCreatedAt());
        chatSessionDto.setUpdatedAt(chatSession.getUpdatedAt());

        return chatSessionDto;
    }
}
