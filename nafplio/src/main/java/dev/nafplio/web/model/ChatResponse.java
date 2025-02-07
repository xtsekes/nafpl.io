package dev.nafplio.web.model;

import java.util.Objects;

public record ChatResponse(ChatResponseType type, String data) {
    public ChatResponse {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
    }
}
