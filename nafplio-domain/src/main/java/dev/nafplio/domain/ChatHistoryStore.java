package dev.nafplio.domain;

public interface ChatHistoryStore {
    PageResult<ChatHistory> get(String chatId, int skip, int take);

    PageResult<ChatHistory> getRecent(String chatId, int skip, int take);

    ChatHistory create(String chatId, String prompt, String response);
}
