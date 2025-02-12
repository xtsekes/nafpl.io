package dev.nafplio.data.implementation;

import dev.nafplio.data.Chat;
import dev.nafplio.data.ChatService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DefaultChatServiceTest {

    @Inject
    ChatService chatService;

    @InjectMock
    ChatRepository chatRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(chatRepository.findByIdOptional("1"))
                .thenReturn(Optional.of(new Chat() {{
                    id = "1";
                    title = "Title";
                    rootDirectory = "Root";
                    createdAt = java.time.LocalDateTime.now();
                }}));
        Mockito.when(chatRepository.listAll())
                .thenReturn(java.util.List.of(new Chat() {{
                    id = "1";
                    title = "Title";
                    rootDirectory = "Root";
                    createdAt = java.time.LocalDateTime.now();
                }}, new Chat() {{
                    id = "2";
                    title = "Title 2";
                    rootDirectory = "Root 2";
                    createdAt = java.time.LocalDateTime.now();
                }}));
    }

    @Test
    void listAllChats() {
        var chats = chatService.get();

        assertNotNull(chats);
        assertEquals(2, chats.size());
        assertEquals("1", chats.get(0).getId());
        assertEquals("2", chats.get(1).getId());
    }

    @Test
    void getById() {
        var chat = chatService.get("1");

        assertNotNull(chat);
        assertEquals("1", chat.get().getId());
    }

    @Test
    void createChat() {
        var createdChat = chatService.create(new Chat() {{
            title = "New Chat";
            rootDirectory = "New Root";
        }});

        assertNotNull(createdChat);
        assertNotNull(createdChat.getId());
        assertEquals("New Chat", createdChat.getTitle());
        assertEquals("New Root", createdChat.getRootDirectory());
    }

    @Test
    void whenIdIsBlank_createChat() {
        var createdChat = chatService.create(new Chat() {{
            id = "";
            title = "New Chat";
            rootDirectory = "New Root";
        }});

        assertNotNull(createdChat);
        assertNotNull(createdChat.getId());
        assertNotEquals("", createdChat.getId());
        assertEquals("New Chat", createdChat.getTitle());
        assertEquals("New Root", createdChat.getRootDirectory());
    }

    @Test
    void whenIdIsGiven_createChat() {
        var createdChat = chatService.create(new Chat() {{
            id = "3";
            title = "New Chat";
            rootDirectory = "New Root";
        }});

        assertNotNull(createdChat);
        assertNotNull(createdChat.getId());
        assertEquals("3", createdChat.getId());
        assertEquals("New Chat", createdChat.getTitle());
        assertEquals("New Root", createdChat.getRootDirectory());
    }
}