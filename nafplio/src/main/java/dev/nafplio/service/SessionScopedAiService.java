package dev.nafplio.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.nafplio.rag.RetrieverFactory;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.SessionScoped;

@RegisterAiService(retrievalAugmentor = RetrieverFactory.class)
@SessionScoped
public interface SessionScopedAiService {
    @SystemMessage("""
                You are a professional software consultant.
                You make software audits for clients.
                Try to answer briefly and precisely, unless asked for details.
            """)
    Multi<String> chat(@MemoryId String chatId, @UserMessage String prompt);
}