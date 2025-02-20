package dev.nafplio.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
class User {
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

