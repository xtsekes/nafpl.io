package dev.nafplio.web.sockets;

import io.quarkus.runtime.util.StringUtil;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.smallrye.jwt.runtime.auth.JsonWebTokenCredential;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
final class JWTAuthMechanism implements HttpAuthenticationMechanism {
    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        var header = context.request().getHeader("upgrade");

        if (StringUtil.isNullOrEmpty(context.request().getHeader("upgrade")) || !header.equalsIgnoreCase("websocket")) { // checks if websocket upgrade
            return Uni.createFrom().optional(Optional.empty());
        }

        var queryParams = parse(context.request().query());
        var jwtToken = queryParams.get("token");

        if (StringUtil.isNullOrEmpty(jwtToken)) {
            return Uni.createFrom().optional(Optional.empty());
        }

        return identityProviderManager
                .authenticate(HttpSecurityUtils.setRoutingContextAttribute(
                        new TokenAuthenticationRequest(new JsonWebTokenCredential(jwtToken)), context));
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return Uni.createFrom().optional(Optional.empty());
    }


    private static Map<String, String> parse(String query) {
        if (query == null) {
            return Map.of();
        }

        Map<String, String> queryPairs = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = null;
            String value = null;

            try {
                key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            queryPairs.put(key, value);
        }
        return queryPairs;
    }
}
