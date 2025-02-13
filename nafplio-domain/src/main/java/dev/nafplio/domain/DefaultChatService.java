package dev.nafplio.domain;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

@ApplicationScoped
@AllArgsConstructor
public final class DefaultChatService implements ChatService {
    private final ChatStore chatStore;

    @Override
    public PageResult<Chat> get(int skip, int take) {
        if (skip < 0 || take <= 0) {
            throw new IllegalArgumentException("Skip must be non-negative and take must be positive");
        }

        return chatStore.get(skip, take);
    }

    @Override
    public Optional<Chat> get(String id) {
        return chatStore.get(id);
    }

    @Override
    public Chat create(Chat chat) {
        if (chat.getId() == null || chat.getId().isBlank()) {
            chat.setId(java.util.UUID.randomUUID().toString());
        }

        if (chat.getCreatedAt() == null) {
            chat.setCreatedAt(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()));
        }

        return chatStore.create(chat);
    }
}
