package dev.nafplio.data;

import dev.nafplio.domain.PageResult;
import dev.nafplio.domain.chat.Chat;
import dev.nafplio.domain.chat.ChatHistoryStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ApplicationScoped
final class ChatHistoryRepository implements PanacheRepository<ChatHistory>, ChatHistoryStore {

    @Override
    public PageResult<dev.nafplio.domain.chat.ChatHistory> get(String chatId, int skip, int take) {
        var query = find("chatId", Sort.by("id"), chatId);

        var count = query.count();
        var data = query
                .page(skip / take, take)
                .stream()
                .map(ChatHistoryRepository::mapToDomain)
                .toList();

        return PageResult.of(skip / take, take, count, data);
    }

    @Override
    public PageResult<dev.nafplio.domain.chat.ChatHistory> getRecent(String chatId, int skip, int take) {
        var query = find("chatId", Sort.by("timestamp").descending(), chatId);

        var count = query.count();
        var data = query
                .page(skip / take, take)
                .stream()
                .map(ChatHistoryRepository::mapToDomain)
                .toList();

        return PageResult.of(skip / take, take, count, data);
    }

    @Override
    @Transactional
    public dev.nafplio.domain.chat.ChatHistory create(String chatId, String prompt, String response) {
        var entity = new ChatHistory();
        entity.setChatId(chatId);
        entity.setPrompt(prompt);
        entity.setResponse(response);
        entity.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        this.persist(entity);

        return mapToDomain(entity);
    }

    @Override
    @Transactional
    public void delete(Chat chat) {
        delete("chatId", chat.getId());
    }

    private static dev.nafplio.domain.chat.ChatHistory mapToDomain(ChatHistory entity) {
        return dev.nafplio.domain.chat.ChatHistory.builder()
                .id(entity.getId().toString())
                .chatId(entity.getChatId())
                .prompt(entity.getPrompt())
                .response(entity.getResponse())
                .timestamp(entity.getTimestamp())
                .build();
    }
}