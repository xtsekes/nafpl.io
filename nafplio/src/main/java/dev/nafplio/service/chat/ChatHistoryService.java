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

    public List<ChatHistoryDto> getChatHistory(String chatId) {
        return chatHistoryRepository.findByChatId(chatId)
                .stream()
                .map(ChatHistoryService::map)
                .toList();
    }

    @Transactional
    public ChatHistoryDto savePrompt(String chatId, String prompt, String response) {
        var chatHistory = new ChatHistory();
        chatHistory.setChatId(chatId);
        chatHistory.setPrompt(prompt);
        chatHistory.setResponse(response);
        chatHistory.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
        chatHistoryRepository.persist(chatHistory);

        return map(chatHistory);
    }

    private static ChatHistoryDto map(ChatHistory chatHistory) {
        var chatHistoryDto = new ChatHistoryDto();

        chatHistoryDto.setId(chatHistory.getId());
        chatHistoryDto.setChatId(chatHistory.getChatId());
        chatHistoryDto.setPrompt(chatHistory.getPrompt());
        chatHistoryDto.setResponse(chatHistory.getResponse());
        chatHistoryDto.setTimestamp(chatHistory.getTimestamp());

        return chatHistoryDto;
    }
}