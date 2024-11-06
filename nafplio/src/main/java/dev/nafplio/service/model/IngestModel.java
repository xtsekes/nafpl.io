package dev.nafplio.service.model;

import java.nio.file.Path;

public record IngestModel(Path injestPath) {

    public static IngestModel of(Path injestPath) {
        return new IngestModel(injestPath);
    }
}
