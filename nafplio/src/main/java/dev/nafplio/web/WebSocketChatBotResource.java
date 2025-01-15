package dev.nafplio.web;

import dev.nafplio.service.SessionScopedAiService;
import dev.nafplio.web.model.ChatResponse;
import dev.nafplio.web.model.ChatResponseType;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(path = "/websocket/{nickname}")
public class WebSocketChatBotResource {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatBotResource.class);

    private final SessionScopedAiService bot;
    private final WebSocketConnection connection;

    public WebSocketChatBotResource(SessionScopedAiService bot, WebSocketConnection connection) {
        this.bot = bot;
        this.connection = connection;
    }

    @OnOpen
    public Multi<ChatResponse> onOpen() {
        var response = Multi.createFrom().item(new ChatResponse(ChatResponseType.DATA, "Hello, how can I help you?"));

        return createMultiResponse(response);
    }

    @OnTextMessage
    public Multi<ChatResponse> onMessage(String message) {
        var response = bot.chat(connection.pathParam("nickname"), message).map(x -> new ChatResponse(ChatResponseType.DATA, x));

        return createMultiResponse(response);
    }

    private Multi<ChatResponse> createMultiResponse(Multi<ChatResponse> actualResponseStream) {
        return Multi.createBy().concatenating().streams(
                Multi.createFrom().item(new ChatResponse(ChatResponseType.INIT, "")),
                actualResponseStream,
                Multi.createFrom().item(new ChatResponse(ChatResponseType.END, ""))
                )
                .onFailure().recoverWithItem(throwable -> {
                    logger.error("An error occurred.", throwable);
                    
                    return new ChatResponse(ChatResponseType.ERROR, "An error occurred.");
                });
    }
}