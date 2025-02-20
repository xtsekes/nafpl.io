package dev.nafplio.http;

import dev.nafplio.auth.*;
import dev.nafplio.http.model.AuthenticationRequest;
import dev.nafplio.http.model.PasswordChangeRequest;
import dev.nafplio.http.model.RegistrationRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Path("/security")
@AllArgsConstructor
public class AuthenticationResource {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRoleService userRoleService;
    private final JwtService tokenGenerator;

    private final ContainerRequestContext requestContext;

    @POST
    @Path("/authenticate")
    @ResponseStatus(200)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
//    @APIResponses({
//            @APIResponse(responseCode = "201", description = "Created", content = @Content(
//                    mediaType = MediaType.APPLICATION_JSON,
//                    schema = @Schema(implementation = Chat.class)
//            )),
//            @APIResponse(responseCode = "400", description = "Bad request"),
//            @APIResponse(responseCode = "500", description = "Internal server error")
//    })
    public String authenticate(@Valid AuthenticationRequest request) {
        var result = authenticationService.authenticate(request.getEmail(), request.getPassword());

        if (!result) {
            throw new ClientErrorException(Response.Status.UNAUTHORIZED);
        }

        var user = userService.getByEmail(request.getEmail())
                .orElseThrow(InternalServerErrorException::new);
        var roles = userRoleService.getRolesForUser(user);

        var claims = Claims.builder()
                .issuer(requestContext.getUriInfo().getBaseUri().toString())
                .subject(user.getId())
                .upn(user.getEmail())
                .preferredUserName(user.getEmail())
                .expiresAt(Instant.now().plus(Duration.ofDays(1)))
                .groups(roles.stream().map(Role::getName).collect(Collectors.toSet()))
                .build();

        return tokenGenerator.generate(claims);
    }

    @POST
    @Path("/register")
    @PermitAll
    @ResponseStatus(200)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User register(@Valid RegistrationRequest request) {
        return authenticationService.register(request.getEmail(), request.getPassword());
    }


    @POST
    @Path("/changePassword")
    @PermitAll
    @ResponseStatus(200)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@Valid PasswordChangeRequest request) {
        var user = userService.getByEmail(request.getEmail());

        if (user.isEmpty()) {
            return Response.serverError().build();
        }

        this.authenticationService.changePassword(user.get(), request.getOldPassword(), request.getPassword());

        return Response.ok().build();
    }
}
