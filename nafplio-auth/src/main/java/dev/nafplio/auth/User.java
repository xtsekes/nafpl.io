package dev.nafplio.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private String id;
    private String email;
    private String normalizedEmail;
}