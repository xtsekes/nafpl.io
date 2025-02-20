package dev.nafplio.auth;

import java.util.Optional;

public interface UserStore {
    Optional<User> get(String id);

    Optional<User> getByNormalizedEmail(String normalizedEmail);

    User add(User user);

    void update(User user);

    void delete(User user);
}