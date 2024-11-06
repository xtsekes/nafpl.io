package dev.nafplio.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

@Singleton
@Startup
public class Ingestor {

    private static final int BATCH_SIZE = 100;

    private final PgVectorEmbeddingStore store;
    private final EmbeddingModel embedding;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Inject
    public Ingestor(PgVectorEmbeddingStore store, EmbeddingModel embedding) {
        this.store = store;
        this.embedding = embedding;
    }

}
