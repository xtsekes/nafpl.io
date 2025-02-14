package dev.nafplio.domain.chat;

import io.quarkus.test.junit.QuarkusTest;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@AllArgsConstructor
class ChatHistoryServiceTest {
    private final ChatService chatService;
    private final ChatHistoryService chatHistoryService;

    @BeforeEach
    void setup() {
        chatService.create(Chat.builder().id("1").title("Title").rootDirectory("Root").build());
        chatService.create(Chat.builder().id("2").title("Title 2").rootDirectory("Root 2").build());

        var seedData = List.of(
                ChatHistory.builder()
                        .id("1")
                        .chatId("1")
                        .prompt("Prompt 1")
                        .response("Message 1")
                        .timestamp(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()))
                        .build(),
                ChatHistory.builder()
                        .id("2")
                        .chatId("1")
                        .prompt("Prompt 2")
                        .response("Message 2")
                        .timestamp(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).minus(Duration.ofDays(1)))
                        .build(),
                ChatHistory.builder()
                        .id("3")
                        .chatId("2")
                        .prompt("Prompt 3")
                        .response("Message 3")
                        .timestamp(LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).minus(Duration.ofDays(2))
                        ).build()
        );

        for (var seedDatum : seedData) {
            chatHistoryService.create(
                    chatService.get(seedDatum.getChatId()).orElseThrow(),
                    seedDatum.getPrompt(),
                    seedDatum.getResponse()
            );
        }
    }

    @AfterEach
    void cleanup() {
        chatService.delete(Chat.builder().id("1").build());
        chatService.delete(Chat.builder().id("2").build());

        chatHistoryService.delete(Chat.builder().id("1").build());
        chatHistoryService.delete(Chat.builder().id("2").build());
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
        assertEquals(page, result.page());
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
        var chat = chatService.get("2").orElseThrow();
        var createdChatHistory = chatHistoryService.create(chat, "New Prompt", "New Message");

        assertNotNull(createdChatHistory);
        assertEquals("New Prompt", createdChatHistory.getPrompt());
        assertEquals("New Message", createdChatHistory.getResponse());

        chatHistoryService.delete(chat);
    }


    @Test
    void whenIdIsNull_createChatHistory() {
        assertThrows(
                NullPointerException.class,
                () -> chatHistoryService.create(null, "New Prompt", "New Message")
        );
    }
}