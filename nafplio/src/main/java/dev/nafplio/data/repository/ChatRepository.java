package dev.nafplio.data.repository;

import dev.nafplio.data.entity.Chat;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatRepository implements PanacheRepositoryBase<Chat, String> {
}
