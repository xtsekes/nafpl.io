package dev.nafplio.service.chat;

import dev.nafplio.data.entity.chat.ChatHistory;
import dev.nafplio.data.repository.ChatHistoryRepository;
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

    public List<ChatHistory> getChatHistory(String chatId) {
        return chatHistoryRepository.findByChatId(chatId);
    }

    @Transactional
    public ChatHistory savePrompt(String chatId, String prompt, String response) {
        var chatHistory = new ChatHistory();
        chatHistory.setChatId(chatId);
        chatHistory.setPrompt(prompt);
        chatHistory.setResponse(response);
        chatHistory.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        chatHistoryRepository.persist(chatHistory);

        return chatHistory;
    }

    @Transactional
    public void updateResponse(Long historyId, String response) {
        var chatHistory = chatHistoryRepository.findById(historyId);
        if (chatHistory == null) {
            throw new IllegalArgumentException("Chat history record not found");
        }
        chatHistory.setResponse(response);
        chatHistoryRepository.persist(chatHistory);
    }
}