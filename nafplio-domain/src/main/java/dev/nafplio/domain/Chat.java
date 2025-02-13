package dev.nafplio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class Chat {
    private String id;

    private String rootDirectory;

    private String title;

    private LocalDateTime createdAt;
}
