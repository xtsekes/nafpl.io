package dev.nafplio.auth;

import dev.nafplio.auth.core.*;
import dev.nafplio.data.Role;
import dev.nafplio.data.User;
import dev.nafplio.data.UserRepository;
import dev.nafplio.data.UserRole;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public final class DefaultUserPasswordService extends UserPasswordService<dev.nafplio.data.User, String> {
    @Inject
    public DefaultUserPasswordService(UserRepository userRepository) {
        super(userRepository, userRepository);
    }
}