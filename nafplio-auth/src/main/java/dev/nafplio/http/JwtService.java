package dev.nafplio.http;

import io.quarkus.runtime.util.StringUtil;
import io.smallrye.config.ConfigMapping;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Getter
@Builder
@AllArgsConstructor
final class Claims {
    private String issuer;
    private String subject;
    private String upn;
    private String preferredUserName;
    private Instant notBefore;
    private Instant issuedAt;
    private Instant expiresAt;
    private Duration expiresIn;
    private Set<String> groups;
    private Set<String> scope;
    private Set<String> audience;
}

@ApplicationScoped
@AllArgsConstructor
final class JwtService {
    private final JwtServiceOptions options;
    private static final String[] SECRET_PREFIXES = new String[]{
            JwtService.class.getCanonicalName()
    };

    public String generate(Claims claims, String... keys) {
        RequireNonNullOrEmpty(claims.getIssuer());
        RequireNonNullOrEmpty(claims.getUpn());

        var issuedAt = claims.getIssuedAt() != null
                ? claims.getIssuedAt()
                : Instant.now();
        var notBefore = claims.getNotBefore() != null
                ? claims.getNotBefore()
                : issuedAt;
        var expiresAt = claims.getExpiresAt() != null
                ? claims.getExpiresAt()
                : LocalDateTime.ofInstant(issuedAt, ZoneOffset.ofHours(0))
                .plusYears(1)
                .atZone(ZoneOffset.ofHours(0))
                .toInstant();

        var claimsBuilder = Jwt
                .issuer(options.getIssuer())
                .subject(claims.getSubject())
                .upn(claims.getUpn())
                .issuedAt(Instant.now())
                .claim(org.eclipse.microprofile.jwt.Claims.nbf, notBefore.getEpochSecond())
                .expiresAt(expiresAt);

        if (claims.getGroups() != null && !claims.getGroups().isEmpty()) {
            claimsBuilder = claimsBuilder.groups(claims.getGroups());
        }

        if (claims.getScope() != null && !claims.getScope().isEmpty()) {
            claimsBuilder = claimsBuilder.scope(claims.getScope());
        }

        if (claims.getAudience() != null && !claims.getAudience().isEmpty()) {
            claimsBuilder = claimsBuilder.audience(claims.getAudience());
        }

        return claimsBuilder
                .sign()
//                .signWithSecret(getSecret(keys))
                ;
    }

    private void RequireNonNullOrEmpty(String obj) {
        Objects.requireNonNull(obj);
    }

    private static String getSecret(String... keys) {
        var finalKeys = Stream
                .concat(Stream.of(SECRET_PREFIXES), Stream.of(keys))
                .filter(x -> !StringUtil.isNullOrEmpty(x))
                .toArray(String[]::new);

        var finalKey = String.join("::", finalKeys);

        var bytes = Base64.getEncoder().encode(finalKey.getBytes());

        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Getter
    @ConfigMapping(prefix = "mp.jwt.verify")
    public static class JwtServiceOptions {
        private String issuer;
    }
}
