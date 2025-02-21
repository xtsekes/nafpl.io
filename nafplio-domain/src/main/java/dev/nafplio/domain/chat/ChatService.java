package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;

import java.util.Optional;

public interface ChatService {
    PageResult<Chat> get(String userId, int skip, int take);

    Optional<Chat> get(String userId, String id);

    Chat create(Chat chat);

    void delete(Chat chat);
}