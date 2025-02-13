package dev.nafplio.domain;

public interface ChatHistoryService {
    PageResult<ChatHistory> get(Chat chat, int skip, int take);

    PageResult<ChatHistory> getRecent(Chat chat, int skip, int take);

    ChatHistory create(Chat chat, String prompt, String response);
}