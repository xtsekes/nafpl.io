package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Mock
@ApplicationScoped
class InMemoryChatHistoryStore implements ChatHistoryStore {
    private final List<ChatHistory> data = new ArrayList<>();

    @Override
    public PageResult<ChatHistory> get(String chatId, int skip, int take) {
        var count = data.stream()
                .filter(x -> x.getChatId().equals(chatId))
                .count();
        var result = data.stream()
                .filter(x -> x.getChatId().equals(chatId))
                .skip(skip)
                .limit(take)
                .toList();

        return PageResult.of(data.size() / take, take, count, result);
    }

    @Override
    public PageResult<ChatHistory> getRecent(String chatId, int skip, int take) {
        var count = data.stream()
                .filter(x -> x.getChatId().equals(chatId))
                .count();
        var result = data.stream()
                .filter(x -> x.getChatId().equals(chatId))
                .sorted((x, y) -> y.getTimestamp().compareTo(x.getTimestamp()))
                .skip(skip)
                .limit(take)
                .toList();

        return PageResult.of((int)count / take, take, count, result);
    }

    @Override
    public ChatHistory create(String chatId, String prompt, String response) {
        var result = ChatHistory.builder()
                .id(UUID.randomUUID().toString())
                .chatId(chatId)
                .prompt(prompt)
                .response(response)
                .timestamp(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()))
                .build();

        data.add(result);
        return result;
    }

    @Override
    public void delete(Chat chat) {
        data.removeIf(x -> x.getChatId().equals(chat.getId()));
    }
}
