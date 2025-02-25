package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

@ApplicationScoped
@AllArgsConstructor
final class DefaultChatService implements ChatService {
    private final ChatStore chatStore;

    @Override
    public PageResult<Chat> get(String userId, int skip, int take) {
        if (skip < 0 || take <= 0) {
            throw new IllegalArgumentException("Skip must be non-negative and take must be positive");
        }

        return chatStore.get(userId, skip, take);
    }

    @Override
    public Optional<Chat> get(String userId, String id) {
        return chatStore.get(userId, id);
    }

    @Override
    public Chat create(Chat chat) {
        Objects.requireNonNull(chat, "Chat is required");

        if (StringUtil.isNullOrEmpty(chat.getUserId())) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (StringUtil.isNullOrEmpty(chat.getId())) {
            chat.setId(java.util.UUID.randomUUID().toString());
        }

        chat.setCreatedAt(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()));

        return chatStore.create(chat);
    }

    @Override
    public void delete(Chat chat) {
        Objects.requireNonNull(chat, "Chat is required");

        if (StringUtil.isNullOrEmpty(chat.getId())) {
            throw new IllegalArgumentException("Chat ID is required");
        }
        
        chatStore.delete(chat);
    }
}
