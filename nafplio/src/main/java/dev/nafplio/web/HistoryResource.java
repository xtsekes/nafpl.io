package dev.nafplio.web;

import dev.nafplio.domain.PageResult;
import dev.nafplio.domain.chat.ChatHistory;
import dev.nafplio.domain.chat.ChatHistoryService;
import dev.nafplio.domain.chat.ChatService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@AllArgsConstructor
@Path("/chats/history")
public class HistoryResource {
    final ChatService chatService;
    final ChatHistoryService chatHistoryService;

    @GET
    @Path("/{chatId}")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public PageResult<ChatHistory> list(@PathParam("chatId") String chatId, @Valid @BeanParam PageParameters pageParameters) {
        var chat = chatService.get(chatId);

        if (chat.isEmpty()) {
            throw new BadRequestException();
        }

        return chatHistoryService.get(chat.get(), (pageParameters.page - 1) * (int) pageParameters.pageSize, pageParameters.pageSize);
    }

    @GET
    @Path("/{chatId}/recent")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public PageResult<ChatHistory> recent(@PathParam("chatId") String chatId, @Valid @BeanParam PageParameters pageParameters) {
        var chat = chatService.get(chatId);

        if (chat.isEmpty()) {
            throw new BadRequestException();
        }

        return chatHistoryService.getRecent(chat.get(), (pageParameters.page - 1) * pageParameters.pageSize, pageParameters.pageSize);
    }
}
