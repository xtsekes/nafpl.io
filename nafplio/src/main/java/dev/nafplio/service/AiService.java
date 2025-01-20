package dev.nafplio.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.nafplio.rag.RetrieverFactory;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService(retrievalAugmentor = RetrieverFactory.class)
public interface AiService {
    @SystemMessage("""
            You are a professional software consultant.
            You make software audits for clients.
            Try to answer briefly and precisely, unless asked for details.
        """)
    @UserMessage("""
        Return results that correspond explicitly to the project with the nickname {nickname} and to the given prompt.
        {prompt}
        """)
    Multi<String> chat(String nickname, String prompt);
}