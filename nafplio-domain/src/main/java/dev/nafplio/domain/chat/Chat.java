package dev.nafplio.domain.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private transient String userId;

    private String rootDirectory;

    private String title;

    private LocalDateTime createdAt;
}
