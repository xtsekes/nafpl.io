package dev.nafplio.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
class Chat {
    @Id
    private String id;

    private String rootDirectory;

    private String title;

    private LocalDateTime createdAt;
}
