package dev.nafplio.auth.core;

import dev.nafplio.auth.*;
import dev.nafplio.auth.impl.Users;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public abstract class UserService<TUser extends User<TKey>, TKey> {
    private final UserStore<TUser, TKey> userStore;
    private final UserPasswordStore<TKey> userPasswordStore;

    public final Optional<TUser> get(TKey id) {
        return this.userStore.get(id);
    }

    public final Optional<TUser> getByEmail(String email) {
        return this.getByNormalizedEmail(Users.normalizeEmail(email));
    }

    public final Optional<TUser> getByNormalizedEmail(String normalizedEmail) {
        return this.userStore.getByNormalizedEmail(normalizedEmail);
    }

    @Transactional
    public final TUser add(TUser user) {
        Objects.requireNonNull(user);

        if (user.getId() != null) {
            this.userStore.get(user.getId())
                    .ifPresent(u -> {
                        throw new UserAlreadyExistsException();
                    });
        }

        return this.userStore.add(user);
    }

    @Transactional
    public final void update(TUser user) {
        Objects.requireNonNull(user);

        this.userStore.get(user.getId())
                .orElseThrow(UserNotFoundException::new);

        this.userStore.update(user);
    }

    @Transactional
    public final void delete(TUser user) {
        Objects.requireNonNull(user);

        this.userStore.get(user.getId())
                .orElseThrow(UserNotFoundException::new);

        this.userStore.delete(user);
    }
}