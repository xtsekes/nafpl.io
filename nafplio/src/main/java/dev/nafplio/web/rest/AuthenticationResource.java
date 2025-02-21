package dev.nafplio.web.rest;

import dev.nafplio.auth.core.*;
import dev.nafplio.auth.jwt.JwtService;
import dev.nafplio.data.Role;
import dev.nafplio.data.User;
import dev.nafplio.data.UserRole;
import dev.nafplio.http.AuthenticationResourceBase;
import dev.nafplio.web.model.AuthenticationRequest;
import dev.nafplio.web.model.PasswordChangeRequest;
import dev.nafplio.web.model.RegistrationRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/security")
public final class AuthenticationResource
        extends AuthenticationResourceBase<UserRole, User, String, Role, String> {
    public AuthenticationResource(
            AuthenticationService<User, String> authenticationService,
            UserService<User, String> userService,
            UserRoleService<UserRole, User, Role, String, String> userRoleService,
            JwtService tokenService) {
        super(authenticationService, userService, userRoleService, tokenService);
    }

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
    public String authenticate(@Valid AuthenticationRequest request, @Context ContainerRequestContext requestContext) {
        return super.authenticate(requestContext, request.getEmail(), request.getPassword());
    }

    @POST
    @Path("/register")
    @PermitAll
    @ResponseStatus(200)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User register(@Valid RegistrationRequest request) {
        return super.register(dev.nafplio.data.User.builder().build(), request.getEmail(), request.getPassword());
    }


    @POST
    @Path("/changePassword")
    @PermitAll
    @ResponseStatus(200)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(@Valid PasswordChangeRequest request) {
        return super.changePassword(request.getEmail(), request.getOldPassword(), request.getPassword());
    }
}