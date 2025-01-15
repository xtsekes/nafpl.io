package dev.nafplio.web;

import dev.nafplio.service.chat.PromptService;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/prompt")
public class PromptResource {

    @Inject
    PromptService promptService;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> test(@QueryParam("nickname") String nickname, @QueryParam("prompt") String prompt, @QueryParam("sessionId") UUID sessionId) {
        return promptService.processPrompt(nickname, prompt, sessionId);
    }
}