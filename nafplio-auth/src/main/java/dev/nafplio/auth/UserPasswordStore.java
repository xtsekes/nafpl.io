package dev.nafplio.auth;

import java.util.Optional;

public interface UserPasswordStore<TKey> {
    Optional<UserCredentials> getCredentials(TKey id);

    void setCredentials(TKey id, UserCredentials credentials);
}
