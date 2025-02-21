package dev.nafplio.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column(unique = true)
    private String email;

    @Email
    @NotNull
    private String normalizedEmail;

    @JsonIgnore
    private String passwordHash;

    @JsonIgnore
    private String securityStamp;
}

