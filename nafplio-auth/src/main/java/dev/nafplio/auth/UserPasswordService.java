package dev.nafplio.auth;

public interface UserPasswordService {
    boolean matches(User user, String password);

    UserCredentials getCredentials(User user);

    void setCredentials(User user, String password);
}