package dev.nafplio.web.rest;

import dev.nafplio.domain.PageResult;
import dev.nafplio.domain.chat.ChatHistory;
import dev.nafplio.domain.chat.ChatHistoryService;
import dev.nafplio.domain.chat.ChatService;
import dev.nafplio.web.UserResolver;
import dev.nafplio.web.rest.model.PageParameters;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/chats/history")
@Authenticated
@AllArgsConstructor
public class HistoryResource {
    private final ChatService chatService;
    private final ChatHistoryService chatHistoryService;
    private final UserResolver userResolver;

    @GET
    @Path("/{chatId}")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public PageResult<ChatHistory> list(@PathParam("chatId") String chatId, @Valid @BeanParam PageParameters pageParameters) {
        var user = userResolver.resolve().orElseThrow(BadRequestException::new);

        var chat = chatService.get(user.getId(), chatId);

        if (chat.isEmpty()) {
            throw new BadRequestException();
        }

        return chatHistoryService.get(chat.get(), (pageParameters.getPage() - 1) * (int) pageParameters.getPageSize(), pageParameters.getPageSize());
    }

    @GET
    @Path("/{chatId}/recent")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK"),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public PageResult<ChatHistory> recent(@PathParam("chatId") String chatId, @Valid @BeanParam PageParameters pageParameters) {
        var user = userResolver.resolve().orElseThrow(BadRequestException::new);

        var chat = chatService.get(user.getId(), chatId);

        if (chat.isEmpty()) {
            throw new BadRequestException();
        }

        return chatHistoryService.getRecent(chat.get(), (pageParameters.getPage() - 1) * pageParameters.getPageSize(), pageParameters.getPageSize());
    }
}
