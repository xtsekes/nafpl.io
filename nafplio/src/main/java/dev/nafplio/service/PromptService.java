package dev.nafplio.service;

import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.nafplio.Unis;
import dev.nafplio.domain.ChatHistoryService;
import dev.nafplio.domain.ChatService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequestScoped
public final class PromptService {
    private static final Logger logger = LoggerFactory.getLogger(PromptService.class);

    private final SessionScopedAiService aiService;
    private final ChatService chatService;
    private final ChatHistoryService chatHistoryService;
    private final ChatMemoryStore chatMemoryStore;

    public PromptService(SessionScopedAiService aiService, ChatService chatService, ChatHistoryService chatHistoryService, ChatMemoryStore chatMemoryStore) {
        Objects.requireNonNull(chatService);
        Objects.requireNonNull(aiService);
        Objects.requireNonNull(chatHistoryService);

        this.aiService = aiService;
        this.chatService = chatService;
        this.chatHistoryService = chatHistoryService;
        this.chatMemoryStore = chatMemoryStore;
    }

    public Multi<String> chat(String chatId, String prompt) {
        Unis.run(createSupplier(chatMemoryStore, chatService, chatHistoryService, chatId));

        var builder = new StringBuilder();

        return aiService.chat(chatId, prompt)
                .onItem().invoke(builder::append)
                .onFailure().invoke(failure -> {
                    logger.error("An Error occurred!", failure);

                    // Save in a single operation
                    Unis.run(() -> {
                        var chat = chatService.get(chatId).orElseThrow();

                        return chatHistoryService.create(chat, prompt, "An Error occurred!");
                    });
                })
                .onCompletion().invoke(() -> {
                    logger.debug("Response: {}", builder);

                    // Save in a single operation
                    Unis.run(() -> {
                        var chat = chatService.get(chatId).orElseThrow();

                        return chatHistoryService.create(chat, prompt, builder.toString());
                    });
                });
    }

    private static Supplier<Object> createSupplier(ChatMemoryStore chatMemoryStore, ChatService chatService, ChatHistoryService chatHistoryService, String chatId) {
        return () -> {
            var memoryStoreMessages = chatMemoryStore.getMessages(chatId);
            if (memoryStoreMessages.isEmpty()) {

                var chat = chatService.get(chatId).orElseThrow();

                memoryStoreMessages = chatHistoryService
                        .getRecent(chat, 0, 10)
                        .data()
                        .stream()
                        .flatMap(history ->
                                Stream.of(
                                        dev.langchain4j.data.message.UserMessage.from(history.getPrompt()),
                                        dev.langchain4j.data.message.AiMessage.from(history.getResponse())
                                ))
                        .toList()
                        .reversed();

                chatMemoryStore.updateMessages(chatId, memoryStoreMessages);
            }

            return null;
        };
    }
}