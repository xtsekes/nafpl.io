package dev.nafplio.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.nafplio.service.model.IngestModel;
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
        Path dir = Path.of(payload.getRootDirectory());
        List<Document> documents = FileSystemDocumentLoader.loadDocumentsRecursively(dir);
        Log.info("Starting ingestion of " + documents.size() + " documents");

        for (int i = 0; i < documents.size(); i += BATCH_SIZE) {
            int fromIndex = i;
            int toIndex = Math.min(i + BATCH_SIZE, documents.size());
            List<Document> batch = documents.subList(fromIndex, toIndex);

            CompletableFuture.runAsync(() -> ingestBatch(batch), executor)
                    .thenRun(() -> Log.info("Finished ingesting batch from index " + fromIndex + " to " + toIndex));
        }
    }

    private void ingestBatch(List<Document> batch) {
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embedding)
                .documentSplitter(recursive(Integer.MAX_VALUE, 0))
                .build();

        ingestor.ingest(batch);
    }
}
