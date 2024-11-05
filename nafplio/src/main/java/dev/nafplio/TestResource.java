package dev.nafplio;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/test")
public class TestResource {

    @Inject
    AiService aiService;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> test() {
        return aiService.chat("hello");
    }


}
