package dev.nafplio.auth.core;

import dev.nafplio.auth.User;
import dev.nafplio.auth.impl.Users;
import io.quarkus.runtime.util.StringUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.security.InvalidParameterException;
import java.util.Objects;

@AllArgsConstructor
public abstract class AuthenticationService<TUser extends User<TKey>, TKey> {
    private final UserService<TUser, TKey> userService;
    private final UserPasswordService<TUser, TKey> userPasswordService;

    public final boolean authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        if (StringUtil.isNullOrEmpty(username)) {
            return false;
        }

        return this.userService.getByEmail(username)
                .filter(value -> userPasswordService.matches(value, password))
                .isPresent();
    }

    @Transactional
    public final TUser register(TUser user, String password) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(password);

        if (StringUtil.isNullOrEmpty(user.getEmail()) || StringUtil.isNullOrEmpty(password)) {
            return null;
        }

        user.setId(null);
        user.setNormalizedEmail(Users.normalizeEmail(user.getEmail()));

        var result = this.userService.add(user);

        this.userPasswordService.setCredentials(result, password);

        return result;
    }

    public final void changePassword(TUser user, String oldPassword, String password) {
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