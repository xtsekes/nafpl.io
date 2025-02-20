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
public final class UserRepository implements PanacheRepositoryBase<User, String>, UserStore<User, String>, UserPasswordStore<String> {

    @Override
    public Optional<User> get(String id) {
        return this.findByIdOptional(id)
                .map(UserRepository::mapToDomain);
    }

    @Override
    public Optional<User> getByNormalizedEmail(String normalizedEmail) {
        return this.find("normalizedEmail", normalizedEmail)
                .firstResultOptional()
                .map(UserRepository::mapToDomain);
    }

    @Override
    public User add(User user) {
        Objects.requireNonNull(user);

        var entity = mapToEntity(user);

        if (StringUtil.isNullOrEmpty(entity.getId())) {
            entity.setId(UUID.randomUUID().toString());
        }

        this.persist(entity);

        return mapToDomain(entity);
    }

    @Override
    public void update(User user) {
        var entity = this.findByIdOptional(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        entity.setEmail(user.getEmail());
        entity.setNormalizedEmail(user.getNormalizedEmail());

        this.persist(entity);
    }

    @Override
    public void delete(User user) {
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

    private static User mapToDomain(User user) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .normalizedEmail(user.getNormalizedEmail())
                .build();
    }

    private static User mapToEntity(User user) {
        var entity = new User();

        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setNormalizedEmail(user.getNormalizedEmail());

        return entity;
    }
}
