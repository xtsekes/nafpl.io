package dev.nafplio.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class ProjectEntity {
    @Id
    @GeneratedValue
    public Long id;

    public String rootDirectory;

    public String nickname;

    public LocalDateTime createdAt;
}
