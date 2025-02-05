package dev.nafplio.web;

import dev.nafplio.Unis;
import dev.nafplio.service.ProjectService;
import io.quarkus.arc.Arc;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.util.StringUtil;
import io.quarkus.websockets.next.HttpUpgradeCheck;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WebSocketHttpUpgradeCheck implements HttpUpgradeCheck {
    @Override
    public Uni<CheckResult> perform(HttpUpgradeContext context) {
        if (!checkAllowed(context)) {
            return CheckResult.rejectUpgrade(400);
        }

        return CheckResult.permitUpgrade();
    }

    @Override
    public boolean appliesTo(String endpointId) {
        var success = HttpUpgradeCheck.super.appliesTo(endpointId);

        if (!success) {
            return false;
        }

        return endpointId.equals(WebSocketChatBotResource.class.getName());
    }

    private static boolean checkAllowed(HttpUpgradeContext context) {
        var webSocketAnnotation = WebSocketChatBotResource.class.getAnnotation(WebSocket.class);

        if (webSocketAnnotation == null) {
            return false;
        }

        var chatId = context.httpRequest().getParam(WebSocketChatBotResource.CHAT_ID_PARAM);

        if (StringUtil.isNullOrEmpty(chatId)) {
            return false;
        }

        return Unis.run(() -> QuarkusTransaction.joiningExisting().call(() -> {
            return Arc.container().instance(ProjectService.class).get().getProjectByNickname(chatId) != null;
        }));
    }
}