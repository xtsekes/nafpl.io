package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mock
@ApplicationScoped
class InMemoryChatStore implements ChatStore {
    private final List<Chat> data = new ArrayList<>();

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
        data.add(chat);
        return chat;
    }

    @Override
    public void delete(Chat chat) {
        data.removeIf(x -> x.getId().equals(chat.getId()));
    }
}
