package dev.nafplio.http;

import dev.nafplio.auth.User;
import dev.nafplio.auth.UserService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;

@Path("/users")
@Authenticated
@AllArgsConstructor
public class UserResource {
    private final UserService userService;

    @GET
    @Path("/current")
    public User current(@Context SecurityContext context) {
        var principal = context.getUserPrincipal();

        return this.userService.getByEmail(principal.getName())
                .orElseThrow(BadRequestException::new);
    }

    @GET
    @RolesAllowed("admin")
    @Path("/{id}")
    public User get(@PathParam("id") String id) {
        return this.userService.get(id)
                .orElseThrow(BadRequestException::new);
    }
}