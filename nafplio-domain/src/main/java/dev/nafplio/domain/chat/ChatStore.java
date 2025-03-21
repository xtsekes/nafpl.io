package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;

import java.util.Optional;

public interface ChatStore {
    PageResult<Chat> get(int skip, int take);

    Optional<Chat> get(String id);

    Chat create(Chat chat);

    void delete(Chat chat);
}
