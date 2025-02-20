package dev.nafplio.http;

import dev.nafplio.auth.*;
import dev.nafplio.auth.core.AuthenticationService;
import dev.nafplio.auth.core.UserRoleService;
import dev.nafplio.auth.core.UserService;
import dev.nafplio.auth.jwt.Claims;
import dev.nafplio.auth.jwt.JwtService;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class AuthenticationResourceBase<
        TUserRole extends UserRole<TUserKey, TRoleKey>,
        TUser extends User<TUserKey>,
        TUserKey,
        TRole extends Role<TRoleKey>,
        TRoleKey> {
    private final AuthenticationService<TUser, TUserKey> authenticationService;
    private final UserService<TUser, TUserKey> userService;
    private final UserRoleService<TUserRole, TUser, TRole, TUserKey, TRoleKey> userRoleService;
    private final JwtService tokenService;

    protected String authenticate(ContainerRequestContext requestContext, String username, String password) {
        var result = authenticationService.authenticate(username, password);

        if (!result) {
            throw new ClientErrorException(Response.Status.UNAUTHORIZED);
        }

        var user = userService.getByEmail(username)
                .orElseThrow(InternalServerErrorException::new);
        var roles = userRoleService.getRolesForUser(user);

        var claims = Claims.builder()
                .issuer(requestContext.getUriInfo().getBaseUri().toString())
                .subject(user.getId().toString())
                .upn(user.getEmail())
                .preferredUserName(user.getEmail())
                .expiresAt(Instant.now().plus(Duration.ofDays(1)))
                .groups(roles.stream().map(Role::getName).collect(Collectors.toSet()))
                .build();

        return tokenService.generate(claims);
    }

    protected TUser register(TUser user, String username, String password) {
        user.setEmail(username);

        return authenticationService.register(user, password);
    }

    protected Response changePassword(String username, String oldPassword, String password) {
        var user = userService.getByEmail(username);

        if (user.isEmpty()) {
            return Response.serverError().build();
        }

        this.authenticationService.changePassword(user.get(), oldPassword, oldPassword);

        return Response.ok().build();
    }
}
