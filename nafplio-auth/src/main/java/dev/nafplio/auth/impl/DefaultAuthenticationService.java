package dev.nafplio.auth.impl;

import dev.nafplio.auth.AuthenticationService;
import dev.nafplio.auth.User;
import dev.nafplio.auth.UserPasswordService;
import dev.nafplio.auth.UserService;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.security.InvalidParameterException;
import java.util.Objects;

@AllArgsConstructor
@ApplicationScoped
final class DefaultAuthenticationService implements AuthenticationService {
    private final UserService userService;
    private final UserPasswordService userPasswordService;

    @Override
    public boolean authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        if (StringUtil.isNullOrEmpty(username)) {
            return false;
        }

        return this.userService.getByEmail(username)
                .filter(value -> userPasswordService.matches(value, password))
                .isPresent();
    }

    @Override
    @Transactional
    public User register(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        if (StringUtil.isNullOrEmpty(username)) {
            return null;
        }

        var user = User.builder()
                .email(username)
                .normalizedEmail(Users.normalizeEmail(username))
                .build();

        var result = this.userService.add(user);

        this.userPasswordService.setCredentials(result, password);

        return result;
    }

    @Override
    public void changePassword(User user, String oldPassword, String password) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(oldPassword);
        Objects.requireNonNull(password);

        if (StringUtil.isNullOrEmpty(oldPassword) || StringUtil.isNullOrEmpty(password)) {
            throw new InvalidParameterException();
        }

        if (!this.authenticate(user.getEmail(), oldPassword)) {
            throw new RuntimeException("Invalid password");
        }

        this.userPasswordService.setCredentials(user, password);
    }
}