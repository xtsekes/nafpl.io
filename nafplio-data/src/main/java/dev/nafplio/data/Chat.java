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
public class Chat {
    @Id
    public String id;

    public String rootDirectory;

    public String title;

    public LocalDateTime createdAt;
}
