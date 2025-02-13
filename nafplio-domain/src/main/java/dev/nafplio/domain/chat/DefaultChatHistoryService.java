package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

import java.util.Objects;

@ApplicationScoped
@AllArgsConstructor
final class DefaultChatHistoryService implements ChatHistoryService {
    private final ChatStore chatStore;
    private final ChatHistoryStore chatHistoryStore;

    @Override
    public PageResult<ChatHistory> get(Chat chat, int skip, int take) {
        Objects.requireNonNull(chat, "Chat is required");

        if (skip < 0 || take <= 0) {
            throw new IllegalArgumentException("Skip must be non-negative and take must be positive");
        }

        return chatHistoryStore.get(chat.getId(), skip, take);
    }

    @Override
    public PageResult<ChatHistory> getRecent(Chat chat, int skip, int take) {
        Objects.requireNonNull(chat, "Chat is required");

        if (skip < 0 || take <= 0) {
            throw new IllegalArgumentException("Skip must be non-negative and take must be positive");
        }

        return chatHistoryStore.getRecent(chat.getId(), skip, take);
    }

    @Override
    public ChatHistory create(Chat chat, String prompt, String response) {
        Objects.requireNonNull(chat, "Chat is required");

        return chatHistoryStore.create(chat.getId(), prompt, response);
    }
}
