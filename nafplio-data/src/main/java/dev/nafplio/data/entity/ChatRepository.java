package dev.nafplio.data.entity;

import dev.nafplio.domain.ChatStore;
import dev.nafplio.domain.PageResult;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public final class ChatRepository implements PanacheRepositoryBase<Chat, String>, ChatStore {
    @Override
    public PageResult<dev.nafplio.domain.Chat> get(int skip, int take) {
        var count = count();
        var data = findAll(Sort.by("id"))
                .page(skip / take, take)
                .stream()
                .map(ChatRepository::mapToDomain)
                .toList();

        return PageResult.of(skip / take, take, count, data);
    }

    @Override
    public Optional<dev.nafplio.domain.Chat> get(String id) {
        return this
                .findByIdOptional(id)
                .map(ChatRepository::mapToDomain);
    }

    @Override
    @Transactional
    public dev.nafplio.domain.Chat create(dev.nafplio.domain.Chat chat) {
        var entity = mapToEntity(chat);

        this.persist(entity);

        return mapToDomain(entity);
    }

    private static dev.nafplio.domain.Chat mapToDomain(dev.nafplio.data.entity.Chat entity) {
        return dev.nafplio.domain.Chat.builder()
                .id(entity.id)
                .rootDirectory(entity.rootDirectory)
                .title(entity.title)
                .createdAt(entity.createdAt)
                .build();
    }

    private static dev.nafplio.data.entity.Chat mapToEntity(dev.nafplio.domain.Chat domain) {
        var entity = new dev.nafplio.data.entity.Chat();

        entity.id = domain.getId();
        entity.rootDirectory = domain.getRootDirectory();
        entity.title = domain.getTitle();
        entity.createdAt = domain.getCreatedAt();

        return entity;
    }
}
