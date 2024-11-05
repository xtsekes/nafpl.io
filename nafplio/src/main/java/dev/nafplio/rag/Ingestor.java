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

    @PostConstruct
    public void startIngestion() {
        Path dir = Path.of("/Users/arndzk/Projects/coffee-app/coffee-app-api");
        List<Document> documents = FileSystemDocumentLoader.loadDocumentsRecursively(dir);
        Log.info("Starting ingestion of " + documents.size() + " documents");

        // Split documents into batches for parallel processing
        for (int i = 0; i < documents.size(); i += BATCH_SIZE) {
            int fromIndex = i;
            int toIndex = Math.min(i + BATCH_SIZE, documents.size());
            List<Document> batch = documents.subList(fromIndex, toIndex);

            // Ingest each batch asynchronously
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
