package dev.nafplio.auth;

public interface AuthenticationService {
    boolean authenticate(String username, String password);

    User register(String username, String password);

    void changePassword(User user, String oldPassword, String password);
}
