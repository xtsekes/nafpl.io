package dev.nafplio.http.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegistrationRequest {
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;
}