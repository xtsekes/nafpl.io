package dev.nafplio.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public final class UserCredentials {
    String passwordHash;
    String securityStamp;
}
