package dev.nafplio.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import dev.nafplio.useCases.ChatIdProvider;
import jakarta.enterprise.context.RequestScoped;

import java.util.Objects;
import java.util.function.Supplier;

@RequestScoped
public final class RetrieverFactory implements Supplier<RetrievalAugmentor> {
    private final ChatIdProvider chatIdProvider;
    private final EmbeddingStore<TextSegment> store;
    private final EmbeddingModel model;

    public RetrieverFactory(EmbeddingStore<TextSegment> store, EmbeddingModel model, ChatIdProvider chatIdProvider) {
        Objects.requireNonNull(store);
        Objects.requireNonNull(model);
        Objects.requireNonNull(chatIdProvider);

        this.chatIdProvider = chatIdProvider;

        this.store = store;
        this.model = model;
    }

    @Override
    public RetrievalAugmentor get() {
        var contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .filter(MetadataFilterBuilder.metadataKey("chatId").isEqualTo(chatIdProvider.Resolve()))
                .maxResults(5)
                .build();

        return DefaultRetrievalAugmentor
                .builder()
                .contentRetriever(contentRetriever)
                .build();
    }
}