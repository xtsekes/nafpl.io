package dev.nafplio.data;

import dev.nafplio.auth.UserCredentials;
import dev.nafplio.auth.UserPasswordStore;
import dev.nafplio.auth.UserStore;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
final class UserRepository implements PanacheRepositoryBase<User, String>, UserStore, UserPasswordStore {

    @Override
    public Optional<dev.nafplio.auth.User> get(String id) {
        return this.findByIdOptional(id)
                .map(UserRepository::mapToDomain);
    }

    @Override
    public Optional<dev.nafplio.auth.User> getByNormalizedEmail(String normalizedEmail) {
        return this.find("normalizedEmail", normalizedEmail)
                .firstResultOptional()
                .map(UserRepository::mapToDomain);
    }

    @Override
    public dev.nafplio.auth.User add(dev.nafplio.auth.User user) {
        Objects.requireNonNull(user);

        var entity = mapToEntity(user);

        if (StringUtil.isNullOrEmpty(entity.getId())) {
            entity.setId(UUID.randomUUID().toString());
        }

        this.persist(entity);

        return mapToDomain(entity);
    }

    @Override
    public void update(dev.nafplio.auth.User user) {
        var entity = this.findByIdOptional(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        entity.setEmail(user.getEmail());
        entity.setNormalizedEmail(user.getNormalizedEmail());

        this.persist(entity);
    }

    @Override
    public void delete(dev.nafplio.auth.User user) {
        var entity = this.findByIdOptional(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        this.delete(entity);
    }

    @Override
    public Optional<UserCredentials> getCredentials(String id) {
        return this.findByIdOptional(id)
                .map(entity -> new UserCredentials(entity.getPasswordHash(), entity.getSecurityStamp()));
    }

    @Override
    public void setCredentials(String id, UserCredentials credentials) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(credentials);

        var entity = this.findByIdOptional(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        entity.setPasswordHash(credentials.getPasswordHash());
        entity.setSecurityStamp(credentials.getSecurityStamp());

        this.persist(entity);
    }

    private static dev.nafplio.auth.User mapToDomain(User user) {
        return dev.nafplio.auth.User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .normalizedEmail(user.getNormalizedEmail())
                .build();
    }

    private static User mapToEntity(dev.nafplio.auth.User user) {
        var entity = new User();

        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setNormalizedEmail(user.getNormalizedEmail());

        return entity;
    }
}
