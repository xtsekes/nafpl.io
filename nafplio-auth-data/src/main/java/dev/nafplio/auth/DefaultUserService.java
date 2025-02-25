package dev.nafplio.auth;

import dev.nafplio.auth.core.UserService;
import dev.nafplio.data.User;
import jakarta.enterprise.context.Dependent;

@Dependent
public final class DefaultUserService extends UserService<User, String> {
    public DefaultUserService(UserStore<User, String> userStore, UserPasswordStore<String> userPasswordStore) {
        super(userStore, userPasswordStore);
    }
}
