package dev.nafplio.web;

import dev.nafplio.data.entity.ChatHistory;
import dev.nafplio.service.ChatHistoryService;
import dev.nafplio.service.model.PageResult;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

import java.util.List;

@Path("/chats/history")
public class HistoryResource {
    final ChatHistoryService chatHistoryService;

    public HistoryResource(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    @GET
    @Path("/{chatId}")
    public List<ChatHistory> list(@PathParam("chatId") String chatId) {
        return chatHistoryService.getHistory(chatId);
    }

    @GET
    @Path("/{chatId}/recent")
    public PageResult<List<ChatHistory>> recent(@PathParam("chatId") String chatId, @QueryParam("pageSize") Integer pageSize, @QueryParam("pageNumber") Integer pageNumber) {
        int skip = (pageNumber != null && pageNumber > 0) ? (pageNumber - 1) * (pageSize != null ? pageSize : 10) : 0;
        int take = (pageSize != null && pageSize > 0) ? pageSize : 10;

        return chatHistoryService.getRecentHistory(chatId, skip, take);
    }
}
