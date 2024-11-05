package dev.nafplio;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface AiService {

    Multi<String> chat(String prompt);
}
