package dev.nafplio.http;

//@Path("/users")
//@Authenticated
//@AllArgsConstructor
//public class UserResource {
//    private final UserService userService;
//
//    @GET
//    @Path("/current")
//    public User current(@Context SecurityContext context) {
//        var principal = context.getUserPrincipal();
//
//        return this.userService.getByEmail(principal.getName())
//                .orElseThrow(BadRequestException::new);
//    }
//
//    @GET
//    @RolesAllowed("admin")
//    @Path("/{id}")
//    public User get(@PathParam("id") String id) {
//        return this.userService.get(id)
//                .orElseThrow(BadRequestException::new);
//    }
//}