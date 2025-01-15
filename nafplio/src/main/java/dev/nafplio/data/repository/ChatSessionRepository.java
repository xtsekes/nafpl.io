package dev.nafplio.data.repository;

import dev.nafplio.data.entity.ChatSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatSessionRepository implements PanacheRepository<ChatSession> {
}
