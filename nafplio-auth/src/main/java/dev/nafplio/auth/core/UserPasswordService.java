package dev.nafplio.auth.core;

import dev.nafplio.auth.*;
import dev.nafplio.auth.impl.Users;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.util.StringUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public abstract class UserPasswordService<TUser extends User<TKey>,TKey> {
    private final UserStore<TUser, TKey> userStore;
    private final UserPasswordStore<TKey> userPasswordStore;

    public final boolean matches(TUser user, String password) {
        Objects.requireNonNull(user);

        var credentials = this.getCredentials(user);

        var plainPassword = credentials.getSecurityStamp() + password;

        return BcryptUtil.matches(plainPassword, credentials.getPasswordHash());
    }

    public final UserCredentials getCredentials(TUser user) {
        Objects.requireNonNull(user);

        var entity = this.userStore.get(user.getId())
                .orElseThrow(UserNotFoundException::new);

        return this.userPasswordStore.getCredentials(entity.getId())
                .orElseThrow(NullPointerException::new);
    }

    @Transactional
    public final void setCredentials(TUser user, String password) {
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