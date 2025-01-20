package dev.nafplio.service.chat;

import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.nafplio.service.SessionScopedAiService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequestScoped
public final class PromptService {
    private static final Logger logger = LoggerFactory.getLogger(PromptService.class);

    private final SessionScopedAiService aiService;
    private final ChatHistoryService chatHistoryService;
    private final ChatMemoryStore chatMemoryStore;

    public PromptService(SessionScopedAiService aiService, ChatHistoryService chatHistoryService, ChatMemoryStore chatMemoryStore) {
        Objects.requireNonNull(aiService);
        Objects.requireNonNull(chatHistoryService);

        this.aiService = aiService;
        this.chatHistoryService = chatHistoryService;
        this.chatMemoryStore = chatMemoryStore;
    }

    public Multi<String> chat(String chatId, String prompt) {
        try {
            Uni.createFrom().item(createSupplier(chatMemoryStore, chatHistoryService, chatId))
                    .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                    .subscribe().asCompletionStage().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        var builder = new StringBuilder();

        return aiService.chat(chatId, prompt)
                .onItem().invoke(builder::append)
                .onFailure().invoke(failure -> {
                    logger.error("An Error occurred!", failure);

                    Uni.createFrom().item(() -> {
                                chatHistoryService.savePrompt(chatId, prompt, "An Error occurred!");

                                return null;
                            })
                            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                            .subscribe().asCompletionStage();
                })
                .onCompletion().invoke(() -> {
                    logger.debug("Response: {}", builder);

                    // Save in a single operation
                    Uni.createFrom().item(() -> {
                                chatHistoryService.savePrompt(chatId, prompt, builder.toString()).getId();

                                return null;
                            })
                            .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
                            .subscribe().asCompletionStage();
                });
    }

    private static Supplier<Object> createSupplier(ChatMemoryStore chatMemoryStore, ChatHistoryService chatHistoryService, String chatId) {
        return () -> {
            var memoryStoreMessages = chatMemoryStore.getMessages(chatId);
            if (memoryStoreMessages.isEmpty()) {

                memoryStoreMessages = chatHistoryService.getChatHistory(chatId)
                        .stream().flatMap(history ->
                                Stream.of(
                                        dev.langchain4j.data.message.UserMessage.from(history.getPrompt()),
                                        dev.langchain4j.data.message.AiMessage.from(history.getResponse())
                                ))
                        .toList();

                chatMemoryStore.updateMessages(chatId, memoryStoreMessages);
            }

            return "";
        };
    }
}