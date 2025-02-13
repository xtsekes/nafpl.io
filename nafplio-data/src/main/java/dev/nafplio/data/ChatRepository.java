package dev.nafplio.data;

import dev.nafplio.domain.chat.ChatStore;
import dev.nafplio.domain.PageResult;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
final class ChatRepository implements PanacheRepositoryBase<Chat, String>, ChatStore {
    @Override
    public PageResult<dev.nafplio.domain.chat.Chat> get(int skip, int take) {
        var count = count();
        var data = findAll(Sort.by("id"))
                .page(skip / take, take)
                .stream()
                .map(ChatRepository::mapToDomain)
                .toList();

        return PageResult.of(skip / take, take, count, data);
    }

    @Override
    public Optional<dev.nafplio.domain.chat.Chat> get(String id) {
        return this
                .findByIdOptional(id)
                .map(ChatRepository::mapToDomain);
    }

    @Override
    @Transactional
    public dev.nafplio.domain.chat.Chat create(dev.nafplio.domain.chat.Chat chat) {
        var entity = mapToEntity(chat);

        this.persist(entity);

        return mapToDomain(entity);
    }

    @Override
    @Transactional
    public void delete(dev.nafplio.domain.chat.Chat chat) {
        this.delete(mapToEntity(chat));
    }

    private static dev.nafplio.domain.chat.Chat mapToDomain(dev.nafplio.data.Chat entity) {
        return dev.nafplio.domain.chat.Chat.builder()
                .id(entity.getId())
                .rootDirectory(entity.getRootDirectory())
                .title(entity.getTitle())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private static dev.nafplio.data.Chat mapToEntity(dev.nafplio.domain.chat.Chat domain) {
        var entity = new dev.nafplio.data.Chat();

        entity.setId(domain.getId());
        entity.setRootDirectory(domain.getRootDirectory());
        entity.setTitle(domain.getTitle());
        entity.setCreatedAt(domain.getCreatedAt());

        return entity;
    }
}
