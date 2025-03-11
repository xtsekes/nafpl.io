package dev.nafplio.service;

import dev.nafplio.data.entity.ChatHistory;
import dev.nafplio.data.repository.ChatHistoryRepository;
import dev.nafplio.service.model.PageResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@ApplicationScoped
public class ChatHistoryService {
    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryService(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public List<ChatHistory> get(String chatId) {
        return chatHistoryRepository.findByChatId(chatId);
    }

    public PageResult<List<ChatHistory>> getRecent(String chatId, int skip, int take) {
        if (skip < 0 || take <= 0) {
            throw new IllegalArgumentException("Skip must be non-negative and take must be positive");
        }

        var result = chatHistoryRepository.findRecentByChatId(chatId, skip, take);

        return PageResult.of(skip / take, take, result.getItem1(), result.getItem2());
    }

    @Transactional
    public ChatHistory create(String chatId, String prompt, String response) {
        var chatHistory = new ChatHistory();
        chatHistory.setChatId(chatId);
        chatHistory.setPrompt(prompt);
        chatHistory.setResponse(response);
        chatHistory.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        chatHistoryRepository.persist(chatHistory);

        return chatHistory;
    }
}