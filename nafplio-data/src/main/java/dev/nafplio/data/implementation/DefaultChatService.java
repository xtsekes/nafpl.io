package dev.nafplio.data.implementation;

import dev.nafplio.data.Chat;
import dev.nafplio.data.ChatService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
final class DefaultChatService implements ChatService {
    private final ChatRepository chatRepository;

    DefaultChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public List<Chat> get() {
        return chatRepository.listAll();
    }

    @Override
    public Optional<Chat> get(String id) {
        return chatRepository.findByIdOptional(id);
    }

    @Transactional
    @Override
    public Chat create(Chat chat) {
        if (chat.id == null || chat.id.isBlank()) {
            chat.setId(java.util.UUID.randomUUID().toString());
        }

        chatRepository.persist(chat);

        return chat;
    }
}
