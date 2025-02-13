package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

class TestChatHistoryStore implements ChatHistoryStore {
    private final List<ChatHistory> data = new ArrayList<>(List.of(
            ChatHistory.builder()
                    .id("1")
                    .chatId("1")
                    .prompt("Prompt 1")
                    .response("Message 1")
                    .timestamp(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()))
                    .build(),
            ChatHistory.builder()
                    .id("2")
                    .chatId("1")
                    .prompt("Prompt 2")
                    .response("Message 2")
                    .timestamp(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).minus(Duration.ofDays(1)))
                    .build(),
            ChatHistory.builder()
                    .id("3")
                    .chatId("2")
                    .prompt("Prompt 3")
                    .response("Message 3")
                    .timestamp(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).minus(Duration.ofDays(2))
                    ).build()
    ));

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

        return PageResult.of(data.size() / take, take, count, result);
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
}
