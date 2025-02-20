package dev.nafplio.ai.response.impl;

import dev.nafplio.ai.response.AIResponseMapper;
import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
final class DeepSeekMapper implements AIResponseMapper {
    private final DeepSeekMapperOptions options;
    private boolean ignore;
    private boolean trimWhitespace;

    public DeepSeekMapper(DeepSeekMapperOptions options) {
        this.options = options;

        this.ignore = false;
        this.trimWhitespace = false;
    }

    @Override
    public boolean allowed() {
        return options.enabled && options.modelId.toLowerCase().contains("deepseek");
    }

    @Override
    public String map(String response) {
        if (response.contains("<think>")) {
            ignore = true;

            return response.substring(0, response.indexOf("<think>"));
        }

        if (response.contains("</think>")) {
            ignore = false;
            trimWhitespace = true;

            return trimWhitespace(response.substring(response.indexOf("</think>") + 8));
        }

        if (ignore) {
            return "";
        }

        if (trimWhitespace) {
            response = trimWhitespace(response);

            if (!response.isEmpty()) {
                trimWhitespace = false;
            }
        }

        return response;
    }

    private static String trimWhitespace(String response) {
        return response.replaceFirst("^\\s*", "");
    }

    @ConfigMapping(prefix = "")
    public static final class DeepSeekMapperOptions {
        @ConfigProperty(name = "dev.nafplio.service.deepseek-mapper.enabled", defaultValue = "true")
        boolean enabled;

        @ConfigProperty(name = "quarkus.langchain4j.ollama.chat-model.model-id")
        String modelId;
    }
}
