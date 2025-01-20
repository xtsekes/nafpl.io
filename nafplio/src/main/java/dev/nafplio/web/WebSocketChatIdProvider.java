package dev.nafplio.web;

import dev.nafplio.useCases.ChatIdProvider;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.SessionScoped;

@SessionScoped
public final class WebSocketChatIdProvider implements ChatIdProvider {
    private final WebSocketConnection connection;

    public WebSocketChatIdProvider(WebSocketConnection connection) {
        this.connection = connection;
    }

    @Override
    public String Resolve() {
        return connection.pathParam("nickname");
    }
}
