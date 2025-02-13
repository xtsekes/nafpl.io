package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;

import java.util.List;
import java.util.Optional;

class TestChatStore implements ChatStore {
    private final List<Chat> data = List.of(
            Chat.builder().id("1").title("Title").rootDirectory("Root").build(),
            Chat.builder().id("2").title("Title 2").rootDirectory("Root 2").build()
    );

    @Override
    public Optional<Chat> get(String id) {
        return data.stream().filter(x -> x.getId().equals(id)).findFirst();
    }

    @Override
    public PageResult<Chat> get(int skip, int take) {
        return PageResult.of(data.size() / take, take, data.size(), data);
    }

    @Override
    public Chat create(Chat chat) {
        return chat;
    }
}
