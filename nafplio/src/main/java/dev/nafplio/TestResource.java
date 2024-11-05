package dev.nafplio;

import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
public class TestResource {

    @Inject
    AiService aiService;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> test(@QueryParam("prompt") String prompt) {
        return aiService.chat(prompt);
    }


}
