package dev.nafplio.service.model;

import java.nio.file.Path;

public record IngestModel(String chatId, Path injestPath) {

    public static IngestModel of(String chatId, Path injestPath) {
        return new IngestModel(chatId, injestPath);
    }
}
