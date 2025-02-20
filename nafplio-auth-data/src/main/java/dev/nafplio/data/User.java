package dev.nafplio.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements dev.nafplio.auth.User<String> {
    @Id
    private String id;

    @Email
    @NotNull
    private String email;

    @Email
    @NotNull
    private String normalizedEmail;

    private String passwordHash;

    private String securityStamp;
}

