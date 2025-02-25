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
    public Optional<Chat> get(String userId, String id) {
        return data.stream().filter(x -> x.getUserId().equals(userId) && x.getId().equals(id)).findFirst();
    }

    @Override
    public PageResult<Chat> get(String userId, int skip, int take) {
        var userResults = data.stream()
                .filter(x -> x.getUserId().equals(userId))
                .toList();

        var totalElements = userResults.size();

        var result = userResults.stream()
                .skip(skip)
                .limit(take)
                .toList();

        return PageResult.of(totalElements / take, take, totalElements, result);
    }

    @Override
    public Chat create(Chat chat) {
        data.add(chat);
        return chat;
    }

    @Override
    public void delete(Chat chat) {
        data.removeIf(x -> x.getUserId().equals(chat.getUserId()) && x.getId().equals(chat.getId()));
    }
}
