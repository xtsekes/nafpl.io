package dev.nafplio.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class HistoryResourceTest {
    @Test
    void get400_when_chat_is_invalid() {
        given()
                .pathParam("chatId", UUID.randomUUID().toString())
                .when().get("/chats/history/{chatId}")
                .then()
                .statusCode(400);
    }
}