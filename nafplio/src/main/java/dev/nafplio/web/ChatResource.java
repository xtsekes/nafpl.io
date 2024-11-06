package dev.nafplio.web;

import dev.nafplio.service.AiService;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
public class ChatResource {

    @Inject
    AiService aiService;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> test(@QueryParam("prompt") String prompt) {
        return aiService.chat(prompt);
    }

}
