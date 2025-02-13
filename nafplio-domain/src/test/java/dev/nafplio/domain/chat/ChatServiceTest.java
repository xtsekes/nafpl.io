package dev.nafplio.domain.chat;

import io.quarkus.test.junit.QuarkusTest;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@AllArgsConstructor
class ChatServiceTest {
    private final ChatService chatService;

    @BeforeEach
    void setup() {
        chatService.create(Chat.builder().id("1").title("Title").rootDirectory("Root").build());
        chatService.create(Chat.builder().id("2").title("Title 2").rootDirectory("Root 2").build());
    }

    @AfterEach
    void cleanup() {
        chatService.delete(Chat.builder().id("1").build());
        chatService.delete(Chat.builder().id("2").build());
    }

    @Test
    void listAllChats() {
        var result = chatService.get(0, 10);

        assertNotNull(result);
        assertEquals(2, result.totalElements());
        assertEquals("1", result.data().get(0).getId());
        assertEquals("2", result.data().get(1).getId());
    }

    @Test
    void getById() {
        var chat = chatService.get("1").orElseThrow();

        assertNotNull(chat);
        assertEquals("1", chat.getId());
    }

    @Test
    void createChat() {
        var createdChat = chatService.create(Chat.builder()
                .title("New Chat")
                .rootDirectory("New Root")
                .build());

        assertNotNull(createdChat);
        assertNotNull(createdChat.getId());
        assertEquals("New Chat", createdChat.getTitle());
        assertEquals("New Root", createdChat.getRootDirectory());

        chatService.delete(createdChat);
    }

    @Test
    void whenIdIsBlank_createChat() {
        var createdChat = chatService.create(Chat.builder()
                .id("")
                .title("New Chat")
                .rootDirectory("New Root")
                .build());

        assertNotNull(createdChat);
        assertNotNull(createdChat.getId());
        assertNotEquals("", createdChat.getId());
        assertEquals("New Chat", createdChat.getTitle());
        assertEquals("New Root", createdChat.getRootDirectory());

        chatService.delete(createdChat);
    }

    @Test
    void whenIdIsGiven_createChat() {
        var createdChat = chatService.create(Chat.builder()
                .id("3")
                .title("New Chat")
                .rootDirectory("New Root")
                .build());

        assertNotNull(createdChat);
        assertNotNull(createdChat.getId());
        assertEquals("3", createdChat.getId());
        assertEquals("New Chat", createdChat.getTitle());
        assertEquals("New Root", createdChat.getRootDirectory());

        chatService.delete(createdChat);
    }
}