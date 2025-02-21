package dev.nafplio.web;

import dev.nafplio.auth.core.UserService;
import dev.nafplio.data.User;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.Dependent;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@Dependent
final class DefaultUserResolver implements UserResolver {
    private final UserService<User, String> userService;
    private final SecurityIdentity currentIdentity;

    @Override
    public Optional<User> resolve() {
        var principal = currentIdentity.getPrincipal();

        return this.userService.getByEmail(principal.getName());
    }
}
