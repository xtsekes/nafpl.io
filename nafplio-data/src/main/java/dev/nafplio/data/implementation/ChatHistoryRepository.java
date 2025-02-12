package dev.nafplio.data.implementation;

import dev.nafplio.data.ChatHistory;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
final class ChatHistoryRepository implements PanacheRepository<ChatHistory> {
    public List<ChatHistory> findByChatId(String chatId) {
        return find("chatId", Sort.by("timestamp"), chatId).list();
    }

    public Tuple2<Long, List<ChatHistory>> findRecentByChatId(String chatId, int skip, int take) {
        if (skip < 0 || take <= 0) {
            throw new IllegalArgumentException("Skip must be non-negative and take must be positive");
        }

        var query = find("chatId", Sort.by("timestamp").descending(), chatId);

        return Tuple2.of(
                query.count(),
                query.page(skip / take, take).list());
    }
}