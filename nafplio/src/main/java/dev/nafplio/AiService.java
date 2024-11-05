package dev.nafplio;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface AiService {

    @UserMessage("${prompt}")
    Multi<String> chat(String prompt);
}
