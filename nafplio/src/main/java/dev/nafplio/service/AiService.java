package dev.nafplio.service;

import dev.nafplio.rag.Retriever;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService(retrievalAugmentor = Retriever.class)
public interface AiService {
    Multi<String> chat(String prompt);
}