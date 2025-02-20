package dev.nafplio.auth;

import java.util.Optional;

public interface UserPasswordStore {
    Optional<UserCredentials> getCredentials(String id);

    void setCredentials(String id, UserCredentials credentials);
}
