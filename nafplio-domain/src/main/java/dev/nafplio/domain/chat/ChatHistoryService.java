package dev.nafplio.domain.chat;

import dev.nafplio.domain.PageResult;

public interface ChatHistoryService {
    PageResult<ChatHistory> get(Chat chat, int skip, int take);

    PageResult<ChatHistory> getRecent(Chat chat, int skip, int take);

    ChatHistory create(Chat chat, String prompt, String response);

    void delete(Chat chat);
}