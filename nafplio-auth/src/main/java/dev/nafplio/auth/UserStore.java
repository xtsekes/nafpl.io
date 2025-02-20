package dev.nafplio.auth;

import java.util.Optional;

public interface UserStore<TUser extends User<TKey>, TKey> {
    Optional<TUser> get(TKey id);

    Optional<TUser> getByNormalizedEmail(String normalizedEmail);

    TUser add(TUser user);

    void update(TUser user);

    void delete(TUser user);
}