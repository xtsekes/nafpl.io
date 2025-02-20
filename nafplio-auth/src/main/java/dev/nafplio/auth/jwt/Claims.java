package dev.nafplio.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public final class Claims {
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
