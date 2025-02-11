package dev.nafplio.data.implementation;

import dev.nafplio.data.ChatHistory;
import dev.nafplio.data.ChatHistoryService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ChatHistoryRepositoryTest {

    @Inject
    ChatHistoryService chatHistoryService;

    @InjectMock
    ChatHistoryRepository chatHistoryRepository;

    @BeforeEach
    void setUp() {
        var mockData = List.of(new ChatHistory() {{
            setId(1L);
            setChatId("1");
            setPrompt("Prompt 1");
            setResponse("Message 1");
            setTimestamp(java.time.LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()));
        }}, new ChatHistory() {{
            setId(2L);
            setChatId("1");
            setPrompt("Prompt 2");
            setResponse("Message 2");
            setTimestamp(java.time.LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).minus(java.time.Duration.ofDays(1)));
        }}, new ChatHistory() {{
            setId(3L);
            setChatId("2");
            setPrompt("Prompt 3");
            setResponse("Message 3");
            setTimestamp(java.time.LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).minus(java.time.Duration.ofDays(2)));
        }});

        // Mock setup for ChatHistoryRepository
        Mockito.when(chatHistoryRepository.findByIdOptional(1L))
                .thenReturn(Optional.of(new ChatHistory() {{
                    setId(1L);
                    setChatId("1");
                    setPrompt("Prompt 1");
                    setResponse("Message 1");
                    setTimestamp(java.time.LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()));
                }}));

        Mockito.when(chatHistoryRepository.listAll())
                .thenReturn(mockData);
        Mockito.when(chatHistoryRepository.findRecentByChatId("1", 0, 2))
                .thenReturn(Tuple2.of(2L, mockData.stream().filter(c -> c.getChatId().equals("1")).toList()));
        Mockito.when(chatHistoryRepository.findRecentByChatId("3", 0, 2))
                .thenReturn(Tuple2.of(0L, List.of()));
        Mockito.when(chatHistoryRepository.findByChatId("1"))
                .thenReturn(mockData.stream().filter(c -> c.getChatId().equals("1")).toList());
    }

    @Test
    void givenChatId_getHistory() {
        var chatHistory = chatHistoryService.get("1");

        assertNotNull(chatHistory);
        assertEquals(2, chatHistory.size());
        assertEquals("1", chatHistory.get(0).getChatId());
        assertEquals("1", chatHistory.get(1).getChatId());
    }

    @Test
    void whenChatIdIsNull_getHistory() {
        var chatHistory = chatHistoryService.get(null);

        assertNotNull(chatHistory);
        assertEquals(0, chatHistory.size());
    }

    @Test
    void whenChatIdIsBlank_getHistory() {
        var chatHistory = chatHistoryService.get("");

        assertNotNull(chatHistory);
        assertEquals(0, chatHistory.size());
    }

    @Test
    void whenChatIdIsNotFound_getHistory() {
        var chatHistory = chatHistoryService.get("3");

        assertNotNull(chatHistory);
        assertEquals(0, chatHistory.size());
    }

    @Test
    void givenChatId_getRecentHistory() {
        var pageSize = 2;
        var page = 0;

        var result = chatHistoryService.getRecent("1", 0, 2);

        assertNotNull(result);
        assertEquals(page, result.pageNumber());
        assertEquals(pageSize, result.pageSize());
        assertEquals(2, result.data().size());

        assertEquals("1", result.data().get(0).getChatId());
        assertEquals("1", result.data().get(1).getChatId());
    }

    @Test
    void whenSkipIsNegative_getRecentHistory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.getRecent("1", -1, 2));
    }

    @Test
    void whenTakeIsZero_getRecentHistory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.getRecent("1", 0, 0)
        );
    }

    @Test
    void whenChatIdIsNull_getRecentHistory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.getRecent(null, 0, 2)
        );
    }

    @Test
    void whenChatIdIsBlank_getRecentHistory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.getRecent("", 0, 2)
        );
    }

    @Test
    void whenChatIdIsNotFound_getRecentHistory() {
        var result = chatHistoryService.getRecent("3", 0, 2);

        assertNotNull(result);
        assertEquals(0, result.totalElements());
    }

    @Test
    void createChatHistory() {
        var createdChatHistory = chatHistoryService.create("1", "New Prompt", "New Message");

        assertNotNull(createdChatHistory);
        assertEquals("New Prompt", createdChatHistory.getPrompt());
        assertEquals("New Message", createdChatHistory.getResponse());
    }


    @Test
    void whenIdIsNull_createChatHistory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.create(null, "New Prompt", "New Message")
        );
    }

    @Test
    void whenIdIsBlank_createChatHistory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> chatHistoryService.create("", "New Prompt", "New Message")
        );
    }
}