package dev.nafplio.auth.impl;

import dev.nafplio.auth.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

//@AllArgsConstructor
//public abstract class BaseUserService<TUser extends User<TKey>, TKey> implements UserService<TUser, TKey> {
//    private final UserStore<TUser, TKey> userStore;
//    private final UserPasswordStore<TKey> userPasswordStore;
//
//    @Override
//    public final Optional<TUser> get(TKey id) {
//        return this.userStore.get(id);
//    }
//
//    @Override
//    public final Optional<TUser> getByEmail(String email) {
//        return this.getByNormalizedEmail(Users.normalizeEmail(email));
//    }
//
//    @Override
//    public final Optional<TUser> getByNormalizedEmail(String normalizedEmail) {
//        return this.userStore.getByNormalizedEmail(normalizedEmail);
//    }
//
//    @Override
//    @Transactional
//    public final TUser add(TUser user) {
//        Objects.requireNonNull(user);
//
//        if (user.getId() != null) {
//            this.userStore.get(user.getId())
//                    .ifPresent(u -> {
//                        throw new UserAlreadyExistsException();
//                    });
//        }
//
//        return this.userStore.add(user);
//    }
//
//    @Override
//    @Transactional
//    public final void update(TUser user) {
//        Objects.requireNonNull(user);
//
//        this.userStore.get(user.getId())
//                .orElseThrow(UserNotFoundException::new);
//
//        this.userStore.update(user);
//    }
//
//    @Override
//    @Transactional
//    public final void delete(TUser user) {
//        Objects.requireNonNull(user);
//
//        this.userStore.get(user.getId())
//                .orElseThrow(UserNotFoundException::new);
//
//        this.userStore.delete(user);
//    }
//}