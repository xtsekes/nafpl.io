package dev.nafplio.web.model;

public record CreateProjectPayload(
        String rootDirectory,
        String nickname
) {
}
