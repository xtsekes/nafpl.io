package dev.nafplio.data;

import java.util.List;

public interface ChatHistoryService {
    List<ChatHistory> get(String chatId);

    PageResult<List<ChatHistory>> getRecent(String chatId, int skip, int take);

    ChatHistory create(String chatId, String prompt, String response);
}
