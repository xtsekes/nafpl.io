package dev.nafplio.data.repository;

import dev.nafplio.data.entity.chat.ChatSession;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ChatSessionRepository implements PanacheRepositoryBase<ChatSession, UUID> {
}
