package dev.nafplio.web;

import dev.nafplio.auth.Role;
import dev.nafplio.auth.core.AuthenticationService;
import dev.nafplio.auth.jwt.Claims;
import dev.nafplio.auth.jwt.JwtService;
import dev.nafplio.data.User;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

@QuarkusTest
class HistoryResourceTest {
    private final JwtService jwtService;

    private String token;

    HistoryResourceTest(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @BeforeEach
    void setup() {
        var claims = Claims.builder()
                .issuer("https://localhost:8080")
                .subject(UUID.randomUUID().toString())
                .upn("test@test.org")
                .preferredUserName("test@test.org")
                .expiresAt(Instant.now().plus(Duration.ofDays(1)))
                //.groups(Set.of())
                .build();

        this.token = jwtService.generate(claims);
    }

    @Test
    void get400_when_chat_is_invalid() {
        given()
                .header("Authorization", "Bearer " + token)
                .pathParam("chatId", UUID.randomUUID().toString())
                .when().get("/chats/history/{chatId}")
                .then()
                .statusCode(400);
    }
}