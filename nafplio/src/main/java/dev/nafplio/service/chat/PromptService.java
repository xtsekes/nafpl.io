package dev.nafplio.service.chat;

import dev.nafplio.data.entity.chat.ChatSession;
import dev.nafplio.data.repository.ChatSessionRepository;
import dev.nafplio.service.AiService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class PromptService {

    private final AiService aiService;
    private final ChatHistoryService chatHistoryService;
    private final ChatSessionRepository chatSessionRepository;

    Logger log = LoggerFactory.getLogger(PromptService.class);

    public PromptService(AiService aiService, ChatHistoryService chatHistoryService) {
        this.aiService = aiService;
        this.chatHistoryService = chatHistoryService;
        this.chatSessionRepository = new ChatSessionRepository();
    }

    public Multi<String> processPrompt(String nickname, String prompt, UUID sessionId) {
        // Perform blocking database operations in a worker thread
        return Uni.createFrom().item(() -> {
                    ChatSession chatSession = chatSessionRepository.findById(sessionId);
                    if (chatSession == null) {
                        throw new IllegalArgumentException("Chat session not found for sessionId: " + sessionId);
                    }
                    return chatSession;
                })
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                .toMulti()
                .flatMap(chatSession -> {
                    Long historyId = chatHistoryService.savePrompt(chatSession, prompt).getId();

                    return aiService.chat(nickname, prompt)
                            .onItem().invoke(chunk -> {
                                log.info(chunk);
                                Uni.createFrom().item(() -> {
                                            chatHistoryService.updateResponse(historyId, chunk);
                                            return null;
                                        })
                                        .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                                        .subscribe().asCompletionStage();
                            });
                });
    }

}
