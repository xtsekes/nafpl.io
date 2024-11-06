package dev.nafplio.web.model;

public record IngestPayload(
        String ingestDirectory,
        String outputDirectory,
        String nickname
) {
}