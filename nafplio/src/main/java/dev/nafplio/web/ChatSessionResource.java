package dev.nafplio.web;

import dev.nafplio.data.entity.chat.ChatSession;
import dev.nafplio.service.chat.ChatSessionService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

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
    public List<ChatSession> getChatSessions() {
        return chatSessionService.getSessionsSorted();
    }

    @POST
    @Path("/create-chat-session")
    public Response createChatSession(@QueryParam("title") String title) {
        ChatSession chatSession = chatSessionService.createSession(title);

        return Response.status(Response.Status.CREATED).entity(chatSession).build();
    }

    @DELETE
    @Path("/delete-chat-session/{id}")
    public Response deleteSession(@PathParam("id") UUID id) {
        chatSessionService.deleteSession(id);

        return Response.noContent().build();
    }
}
