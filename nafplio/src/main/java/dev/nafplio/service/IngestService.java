package dev.nafplio.service;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import dev.nafplio.service.model.IngestModel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

@ApplicationScoped
public class IngestService {
    private final PgVectorEmbeddingStore store;
    private final EmbeddingModel embedding;

    public IngestService(PgVectorEmbeddingStore store, EmbeddingModel embedding) {
        this.store = store;
        this.embedding = embedding;
    }

    public void startIngestion(IngestModel payload) {
        var documents = FileSystemDocumentLoader.loadDocuments(payload.injestPath());

        Log.info("Starting ingestion of document");

        EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .embeddingModel(embedding)
                .documentTransformer(document -> {
                    document.metadata().put("chatId", payload.chatId());

                    return document;
                })
                .documentSplitter(recursive(2000, 200))
                .build()
                .ingest(documents);

        Log.info("Finished ingesting all document");
    }
}