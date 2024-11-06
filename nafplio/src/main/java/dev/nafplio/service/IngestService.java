package dev.nafplio.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.nafplio.service.model.IngestModel;
import dev.nafplio.web.model.IngestPayload;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

@ApplicationScoped
public class IngestService {

    private static final int BATCH_SIZE = 100;

    private final PgVectorEmbeddingStore store;
    private final EmbeddingModel embedding;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public IngestService(PgVectorEmbeddingStore store, EmbeddingModel embedding) {
        this.store = store;
        this.embedding = embedding;
    }

    public void startIngestion(IngestModel payload) {
        Path dir = Path.of(payload.getOutputDirectory());
        Document document = FileSystemDocumentLoader.loadDocument(dir);

        Log.info("Starting ingestion of document");
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embedding)
                .documentSplitter(recursive(500, 50))
                .build();
        ingestor.ingest(document);
        Log.info("Finished ingesting all document");
    }
}
