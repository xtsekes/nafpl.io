package dev.nafplio.service.chat;

import dev.nafplio.data.entity.chat.ChatHistory;
import dev.nafplio.data.entity.chat.ChatSession;
import dev.nafplio.data.repository.ChatHistoryRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryService(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public List<ChatHistory> getChatHistoryBySessionId(UUID sessionId) {
        return chatHistoryRepository.findBySessionId(sessionId);
    }

    @Transactional
    public ChatHistory savePrompt(ChatSession chatSession, String prompt) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setChatsession(chatSession);
        chatHistory.setPrompt(prompt);
        chatHistory.setResponse(null);
        chatHistory.setTimestamp(LocalDateTime.now());
        chatHistoryRepository.persist(chatHistory);

        return chatHistory;
    }

    @Transactional
    public void updateResponse(Long historyId, String response) {
        ChatHistory chatHistory = chatHistoryRepository.findById(historyId);
        if (chatHistory == null) {
            throw new IllegalArgumentException("Chat history record not found");
        }
        chatHistory.setResponse(response);
        chatHistoryRepository.persist(chatHistory);
    }
}
