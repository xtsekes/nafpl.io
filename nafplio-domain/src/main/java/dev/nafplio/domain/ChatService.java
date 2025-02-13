package dev.nafplio.domain;

import java.util.Optional;

public interface ChatService {
    PageResult<Chat> get(int skip, int take);

    Optional<Chat> get(String id);

    Chat create(Chat chat);
}
