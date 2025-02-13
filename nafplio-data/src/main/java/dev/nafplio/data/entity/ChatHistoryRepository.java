package dev.nafplio.data.entity;

import dev.nafplio.domain.ChatHistoryStore;
import dev.nafplio.domain.PageResult;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@ApplicationScoped
public final class ChatHistoryRepository implements PanacheRepository<ChatHistory>, ChatHistoryStore {
    public List<ChatHistory> findByChatId(String chatId) {
        return find("chatId", Sort.by("timestamp"), chatId).list();
    }

    @Override
    public PageResult<dev.nafplio.domain.ChatHistory> get(String chatId, int skip, int take) {
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
    public PageResult<dev.nafplio.domain.ChatHistory> getRecent(String chatId, int skip, int take) {
        var query = find("chatId", Sort.by("timestamp").descending(), chatId);

        var count = query.count();
        var data = query
                .page(skip / take, take)
                .stream()
                .map(ChatHistoryRepository::mapToDomain)
                .toList();

        return PageResult.of(skip / take, take, count, data);
    }

    @Transactional
    @Override
    public dev.nafplio.domain.ChatHistory create(String chatId, String prompt, String response) {
        var entity = new dev.nafplio.data.entity.ChatHistory();
        entity.setChatId(chatId);
        entity.setPrompt(prompt);
        entity.setResponse(response);
        entity.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));

        this.persist(entity);

        return mapToDomain(entity);
    }

    private static dev.nafplio.domain.ChatHistory mapToDomain(dev.nafplio.data.entity.ChatHistory entity) {
        return dev.nafplio.domain.ChatHistory.builder()
                .chatId(entity.getChatId())
                .prompt(entity.getPrompt())
                .response(entity.getResponse())
                .timestamp(entity.getTimestamp())
                .build();
    }
}