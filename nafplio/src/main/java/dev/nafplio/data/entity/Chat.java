package dev.nafplio.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Chat {
    @Id
    public String id;

    public String rootDirectory;

    public String title;

    public LocalDateTime createdAt;
}
