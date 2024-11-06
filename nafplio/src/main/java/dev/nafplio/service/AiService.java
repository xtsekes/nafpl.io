package dev.nafplio.service;

import dev.langchain4j.service.SystemMessage;
import dev.nafplio.rag.Retriever;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService(retrievalAugmentor = Retriever.class)
public interface AiService {
    @SystemMessage("""
            You are a professional software consultant.
            You make software audits for clients.
            Try to answer briefly and precisely, unless asked for details.
        """)
    Multi<String> chat(String prompt);
}