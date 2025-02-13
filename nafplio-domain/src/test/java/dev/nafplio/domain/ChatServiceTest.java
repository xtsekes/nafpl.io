package dev.nafplio.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatServiceTest {
    private final ChatService chatService;

    ChatServiceTest() {
        this.chatService = new DefaultChatService(new TestChatStore());
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
        var chat = chatService.get("1");

        assertNotNull(chat);
        assertEquals("1", chat.get().getId());
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
    }
}