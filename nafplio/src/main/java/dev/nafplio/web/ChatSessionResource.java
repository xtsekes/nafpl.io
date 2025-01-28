package dev.nafplio.web;

import dev.nafplio.service.chat.ChatSessionDto;
import dev.nafplio.service.chat.ChatSessionService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatSessionResource {

    private final ChatSessionService chatSessionService;

    public ChatSessionResource(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @GET
    @Path("/get-all-chat-sessions")
    public List<ChatSessionDto> getChatSessions() {
        return chatSessionService.getSessionsSorted();
    }

    @POST
    @Path("/create-chat-session")
    public Response createChatSession(@QueryParam("title") String title) {
        var chatSession = chatSessionService.createSession(title);

        return Response.status(Response.Status.CREATED).entity(chatSession).build();
    }

    @DELETE
    @Path("/delete-chat-session/{id}")
    public Response deleteSession(@PathParam("id") String id) {
        chatSessionService.deleteSession(id);

        return Response.noContent().build();
    }
}
