package dev.nafplio.data;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    List<Chat> get();

    Optional<Chat> get(String id);

    Chat create(Chat chat);
}
