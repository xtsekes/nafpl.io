package dev.nafplio.web.model;

public record IngestPayload(
        String rootDirectory,
        String nickname
) {
}
