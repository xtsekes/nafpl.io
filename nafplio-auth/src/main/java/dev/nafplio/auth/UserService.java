package dev.nafplio.auth;

import java.util.Optional;

public interface UserService {
    Optional<User> get(String id);

    Optional<User> getByEmail(String email);

    Optional<User> getByNormalizedEmail(String normalizedEmail);

    User add(User user);

    void update(User user);

    void delete(User user);
}