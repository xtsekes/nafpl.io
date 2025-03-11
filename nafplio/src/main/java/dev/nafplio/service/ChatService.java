package dev.nafplio.service;

import dev.nafplio.data.entity.Chat;
import dev.nafplio.data.repository.ChatRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> get() {
        return chatRepository.listAll();
    }

    public Optional<Chat> get(String id) {
        return chatRepository.findByIdOptional(id);
    }

    @Transactional
    public Chat create(Chat chat) {
        if (chat.id == null || chat.id.isBlank()) {
            chat.setId(java.util.UUID.randomUUID().toString());
        }

        chatRepository.persist(chat);

        return chat;
    }
}
