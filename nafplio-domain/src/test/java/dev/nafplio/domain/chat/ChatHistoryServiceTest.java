package dev.nafplio.domain.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatHistoryServiceTest {
    private final ChatService chatService;
    private final ChatHistoryService chatHistoryService;

    public ChatHistoryServiceTest() {
        var chatStore = new TestChatStore();
        chatService = new DefaultChatService(chatStore);
        chatHistoryService = new DefaultChatHistoryService(chatStore, new TestChatHistoryStore());
    }

    @Test
    void givenChatId_getHistory() {
        var chat = chatService.get("1").orElseThrow();
        var chatHistory = chatHistoryService.get(chat, 0, 100);

        assertNotNull(chatHistory);
        assertEquals(2, chatHistory.totalElements());
        assertEquals("1", chatHistory.data().get(0).getChatId());
        assertEquals("1", chatHistory.data().get(1).getChatId());
    }

    @Test
    void whenChatIdIsNull_getHistory() {
        assertThrows(
                NullPointerException.class,
                () -> chatHistoryService.get(null, 0, 100));
    }

    @Test
    void whenChatIdIsNotFound_getHistory() {
        var result = chatHistoryService.getRecent(Chat.builder().id("3").build(), 0, 100);

        assertNotNull(result);
        assertEquals(0, result.totalElements());
    }

    @Test
    void givenChatId_getRecentHistory() {
        var pageSize = 2;
        var page = 1;

        var chat = chatService.get("1").orElseThrow();
        var result = chatHistoryService.getRecent(chat, (page - 1) * pageSize, pageSize);

        assertNotNull(result);
        assertEquals(page, result.pageNumber());
        assertEquals(pageSize, result.pageSize());
        assertEquals(2, result.data().size());

        assertEquals("1", result.data().get(0).getChatId());
        assertEquals("1", result.data().get(1).getChatId());
    }

    @Test
    void whenSkipIsNegative_getHistory() {
        var chat = chatService.get("1").orElseThrow();

        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.get(chat, -1, 2));
    }

    @Test
    void whenTakeIsNegative_getHistory() {
        var chat = chatService.get("1").orElseThrow();

        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.get(chat, 1, -2));
    }

    @Test
    void whenSkipIsNegative_getRecentHistory() {
        var chat = chatService.get("1").orElseThrow();

        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.getRecent(chat, -1, 2));
    }

    @Test
    void whenTakeIsNegative_getRecentHistory() {
        var chat = chatService.get("1").orElseThrow();

        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.getRecent(chat, 1, -2));
    }


    @Test
    void whenTakeIsZero_getRecentHistory() {
        var chat = chatService.get("1").orElseThrow();

        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.getRecent(chat, 0, 0)
        );
    }

    @Test
    void whenChatIdIsNull_getRecentHistory() {
        assertThrows(
                NullPointerException.class,
                () -> chatHistoryService.getRecent(null, 0, 2)
        );
    }

    @Test
    void createChatHistory() {
        var chat = chatService.get("1").orElseThrow();
        var createdChatHistory = chatHistoryService.create(chat, "New Prompt", "New Message");

        assertNotNull(createdChatHistory);
        assertEquals("New Prompt", createdChatHistory.getPrompt());
        assertEquals("New Message", createdChatHistory.getResponse());
    }


    @Test
    void whenIdIsNull_createChatHistory() {
        assertThrows(
                NullPointerException.class,
                () -> chatHistoryService.create(null, "New Prompt", "New Message")
        );
    }
}