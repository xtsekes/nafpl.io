package dev.nafplio.web;

import dev.nafplio.domain.ChatHistoryService;
import dev.nafplio.domain.ChatService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Path("/chats/history")
public class HistoryResource {
    final ChatService chatService;
    final ChatHistoryService chatHistoryService;

    @GET
    @Path("/{chatId}")
    public Response list(@PathParam("chatId") String chatId, @QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber) {
        // set defaults
        pageNumber = pageNumber != null ? pageNumber : 1;
        pageSize = pageSize != null ? pageSize : 100;

        // validate
        if (pageNumber <= 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        if (pageSize <= 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        var chat = chatService.get(chatId);

        if (chat.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(chatHistoryService.get(chat.get(), (pageNumber - 1) * pageSize, pageSize))
                .build();
    }

    @GET
    @Path("/{chatId}/recent")
    public Response recent(@PathParam("chatId") String chatId, @QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber) {
        // set defaults
        pageNumber = pageNumber != null ? pageNumber : 1;
        pageSize = pageSize != null ? pageSize : 100;

        // validate
        if (pageNumber <= 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        if (pageSize <= 0) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        var chat = chatService.get(chatId);

        if (chat.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(chatHistoryService.getRecent(chat.get(), (pageNumber - 1) * pageSize, pageSize))
                .build();
    }
}
