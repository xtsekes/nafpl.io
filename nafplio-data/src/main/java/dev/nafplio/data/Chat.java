package dev.nafplio.data;

import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private String userId;

    private String rootDirectory;

    private String title;

    private LocalDateTime createdAt;
}
