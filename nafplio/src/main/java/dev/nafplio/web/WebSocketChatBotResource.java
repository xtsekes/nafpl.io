package dev.nafplio.web;

import dev.nafplio.Unis;
import dev.nafplio.domain.chat.ChatHistoryService;
import dev.nafplio.domain.chat.ChatService;
import dev.nafplio.service.PromptService;
import dev.nafplio.useCases.ChatIdProvider;
import dev.nafplio.web.model.ChatResponse;
import dev.nafplio.web.model.ChatResponseType;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.mutiny.Multi;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket(path = "/websocket/{" + WebSocketChatBotResource.CHAT_ID_PARAM + "}")
@AllArgsConstructor
public class WebSocketChatBotResource {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketChatBotResource.class);

    public static final String CHAT_ID_PARAM = "chatId";

    private final ChatService chatService;
    private final ChatHistoryService chatHistoryService;
    private final PromptService promptService;
    private final ChatIdProvider chatIdProvider;

    @OnOpen
    public Multi<ChatResponse> onOpen() {
        var chatId = chatIdProvider.Resolve();

        var hasHistory = Unis.run(() -> {
            var chat = chatService.get(chatId).orElseThrow();

            return chatHistoryService.getRecent(chat, 0, 1).totalElements() > 0;
        });

        if(hasHistory) {
            return null;
        }

        var response = Multi.createFrom().item(new ChatResponse(ChatResponseType.DATA, "Hello, how can I help you?"));

        return createMultiResponse(response);
    }

    @OnTextMessage
    public Multi<ChatResponse> onMessage(String message) {
        var response = promptService.chat(chatIdProvider.Resolve(), message).map(x -> new ChatResponse(ChatResponseType.DATA, x));

        return createMultiResponse(response);
    }

    private static Multi<ChatResponse> createMultiResponse(Multi<ChatResponse> actualResponseStream) {
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