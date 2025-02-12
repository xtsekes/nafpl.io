package dev.nafplio.data.implementation;

import dev.nafplio.data.Chat;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
final class ChatRepository implements PanacheRepositoryBase<Chat, String> {
}
