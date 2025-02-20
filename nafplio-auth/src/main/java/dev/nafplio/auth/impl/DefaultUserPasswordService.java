package dev.nafplio.auth.impl;

import dev.nafplio.auth.*;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@ApplicationScoped
final class DefaultUserPasswordService implements UserPasswordService {
    private final UserStore userStore;
    private final UserPasswordStore userPasswordStore;

    @Override
    public boolean matches(User user, String password) {
        Objects.requireNonNull(user);

        var credentials = this.getCredentials(user);

        var plainPassword = credentials.getSecurityStamp() + password;

        return BcryptUtil.matches(plainPassword, credentials.getPasswordHash());
    }

    @Override
    public UserCredentials getCredentials(User user) {
        Objects.requireNonNull(user);

        var entity = this.userStore.get(user.getId())
                .orElseThrow(UserNotFoundException::new);

        return this.userPasswordStore.getCredentials(entity.getId())
                .orElseThrow(NullPointerException::new);
    }

    @Transactional
    public void setCredentials(User user, String password) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(password);

        if (StringUtil.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        var entity = this.userStore.get(user.getId())
                .orElseThrow(UserNotFoundException::new);

        var securityStamp = Users.generateSecurityStamp();
        var passwordHash = hash(securityStamp, password);

        this.userPasswordStore.setCredentials(entity.getId(), UserCredentials.builder()
                .securityStamp(securityStamp)
                .passwordHash(passwordHash)
                .build());
    }

    private String hash(String securityStamp, String password) {
        var plainPassword = securityStamp + password;

        return BcryptUtil.bcryptHash(plainPassword);
    }
}
