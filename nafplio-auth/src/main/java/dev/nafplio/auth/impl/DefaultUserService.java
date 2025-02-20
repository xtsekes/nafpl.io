package dev.nafplio.auth.impl;

import dev.nafplio.auth.*;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@ApplicationScoped
final class DefaultUserService implements UserService {
    private final UserStore userStore;
    private final UserPasswordStore userPasswordStore;

    @Override
    public Optional<User> get(String id) {
        return this.userStore.get(id);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return this.getByNormalizedEmail(Users.normalizeEmail(email));
    }

    @Override
    public Optional<User> getByNormalizedEmail(String normalizedEmail) {
        return this.userStore.getByNormalizedEmail(normalizedEmail);
    }

    @Override
    @Transactional
    public User add(User user) {
        Objects.requireNonNull(user);

        if (!StringUtil.isNullOrEmpty(user.getId())) {
            this.userStore.get(user.getId())
                    .ifPresent(u -> {
                        throw new UserAlreadyExistsException();
                    });
        }

        return this.userStore.add(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        Objects.requireNonNull(user);

        this.userStore.get(user.getId())
                .orElseThrow(UserNotFoundException::new);

        this.userStore.update(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        Objects.requireNonNull(user);

        this.userStore.get(user.getId())
                .orElseThrow(UserNotFoundException::new);

        this.userStore.delete(user);
    }
}

