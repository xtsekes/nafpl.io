package dev.nafplio.auth;

import dev.nafplio.auth.core.AuthenticationService;
import dev.nafplio.data.User;
import jakarta.enterprise.context.Dependent;

@Dependent
final class DefaultAuthenticationService extends AuthenticationService<User, String> {
    public DefaultAuthenticationService(DefaultUserService userService, DefaultUserPasswordService userPasswordService) {
        super(userService, userPasswordService);
    }
}
